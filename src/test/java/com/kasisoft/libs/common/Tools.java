package com.kasisoft.libs.common;

import com.kasisoft.libs.common.io.IoSupport;

import com.kasisoft.libs.common.text.StringFBuilder;
import com.kasisoft.libs.common.text.StringFunctions;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.Modifier;
import java.lang.reflect.TypeVariable;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Tools {

  @Data @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class ParamRecord {
    
    boolean         typeVar;
    String          name;
    String          type;
    List<String>    annotations;
    
  } /* ENDCLASS */
  
  @Data @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class MethodRecord implements Comparable<MethodRecord> {
    
    String              name;
    List<ParamRecord>   params;
    Class<?>            returnType;
    String              returnTypeName;
    List<String>        returnAnnotations;
    
    @Override
    public int compareTo(MethodRecord o) {
      int result = name.compareTo(o.getName());
      if (result == 0) {
        result = Integer.compare(params.size(), o.getParams().size());
      }
      return result;
    }
    
  } /* ENDCLASS */
  
  private static Pattern VALUE_PATTERN = Pattern.compile("\\Qvalue=\\E([^ ,)]+)");
  
  private static void ioSupport() throws Exception {
    
    var ioSupportClass = IoSupport.class;
    
    var methodRecords = new ArrayList<MethodRecord>();
    var methods = ioSupportClass.getDeclaredMethods();
    for (var m : methods) {
      
      if ((m.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
        
        var params = new ArrayList<ParamRecord>();
        for (var p : m.getParameters()) {
          
          var typeVar   = p.getParameterizedType() instanceof TypeVariable;
          var typeName  = toType(p.getParameterizedType().getTypeName());
          
          params.add(new ParamRecord(typeVar, p.getName(), typeName, toAnnotations(p)));
          
        }

        String returnTypeName = null;
        if (m.getAnnotatedReturnType() instanceof AnnotatedTypeVariable) {
          AnnotatedTypeVariable a = (AnnotatedTypeVariable) m.getAnnotatedReturnType();
          returnTypeName =  a.getType().getTypeName();
        }
        
        methodRecords.add(new MethodRecord(m.getName(), params, m.getReturnType(), returnTypeName, toAnnotations(m.getAnnotatedReturnType())));
        
      }
    }
    
    Collections.sort(methodRecords);
    methodRecords.forEach(Tools::ioSupportDump);
    
  }
  
  private static String toType(String typeName) {
    var idxOpen   = typeName.indexOf('<');
    var idxClose  = typeName.lastIndexOf('>');
    String mainType  = null;
    String paramType = null;
    if ((idxOpen != -1) && (idxClose != -1)) {
      String innerPortion = typeName.substring(idxOpen + 1, idxClose);
      String[] parts = innerPortion.split(",");
      for (var i = 0; i < parts.length; i++) {
        parts[i] = toType(parts[i]);
      }
      paramType = StringFunctions.concatenate(", ", parts);
      mainType = typeName.substring(0, idxOpen);
    } else {
      mainType = typeName;
    }
    int lidx = mainType.lastIndexOf('.');
    if (lidx != -1) {
      mainType = mainType.substring(lidx + 1);
    }
    if (paramType != null) {
      return mainType + "<" + paramType + ">";
    } else {
      return mainType;
    }
  }
  
  private static void ioSupportDump(MethodRecord record) {
    ioSupportDump(record, "IO_PATH", "Path", false);
    ioSupportDump(record, "IO_FILE", "File", false);
    ioSupportDump(record, "IO_URI", "URI", false);
    ioSupportDump(record, "IO_URL", "URL", true);
  }

  private static void ioSupportDump(MethodRecord record, String member, String type, boolean onlyRead) {
    if (onlyRead) {
      String name = record.getName();
      if (name.startsWith("save") || name.startsWith("write") || name.startsWith("forWriter") || name.startsWith("forOutput") || name.startsWith("newOutput") || name.startsWith("newWriter")) {
        return;
      }
    }
    if (record.getName().endsWith("Impl")) {
      return;
    }
    var builder = new StringFBuilder();
    builder.append("  /**\n");
    builder.appendF("   * @see IoSupport#%s(Object, ", record.getName());
    for (var i = 1; i < record.getParams().size(); i++) {
      builder.appendF("%s, ", record.getParams().get(i).getType());
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.deleteCharAt(builder.length() - 1);
    builder.append(")\n");
    builder.append("   */\n");
    builder.appendF("  public static ");
    if (record.getReturnType() == Void.TYPE) {
      builder.append("void ");
    } else {
      if (record.getReturnTypeName() != null) {
        builder.appendF("<%s> ", record.getReturnTypeName());
      }
      if (!record.getReturnAnnotations().isEmpty()) {
        record.getReturnAnnotations().forEach($ -> builder.append($).append(' '));
      }
      if (record.getReturnTypeName() != null) {
        builder.append(record.getReturnTypeName()).append(' ');
      } else {
        builder.append(record.getReturnType().getSimpleName()).append(' ');
      }
    }
    builder.appendF("%s(", record.getName());
    
    for (var param : record.getParams()) {
      if (!param.getAnnotations().isEmpty()) {
        param.getAnnotations().forEach($ -> builder.append($).append(' '));
      }
      if (param.isTypeVar()) {
        builder.append(type);
      } else {
        builder.append(param.getType());
      }
      builder.appendF(" %s, ", param.getName());
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.deleteCharAt(builder.length() - 1);
    
    builder.appendF(") {\n    ");
    if (record.getReturnType() != Void.TYPE) {
      builder.append("return ");
    }
    builder.appendF("%s.%s(", member, record.getName());
    for (var param : record.getParams()) {
      builder.appendF("%s, ", param.getName());
    }
    builder.deleteCharAt(builder.length() - 1);
    builder.deleteCharAt(builder.length() - 1);
    builder.appendF(");\n");
    builder.append("  }\n");
    
    System.err.println(builder);
    
  }
  
  private static List<String> toAnnotations(AnnotatedElement element) {
    var result = new ArrayList<String>();
    for (var a : element.getAnnotations()) {
      var annotationName = a.annotationType().getSimpleName();
      var rep      = a.toString();
      var suffix    = "";
      var matcher = VALUE_PATTERN.matcher(rep);
      if (matcher.find()) {
        suffix = "(" + matcher.group(1) + ")";
      }
      result.add(String.format("@%s%s", annotationName, suffix));
    }
    return result;
  }
  
  
  public static void main(String[] args) throws Exception {
    
    ioSupport();
    
  }
  
} /* ENDCLASS */
