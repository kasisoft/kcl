package com.kasisoft.libs.common;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.text.*;

import java.util.regex.*;

import java.util.*;

import java.lang.reflect.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Tools {

  private static class ParamRecord {
    
    private boolean         typeVar;
    private String          name;
    private String          type;
    private List<String>    annotations;

    public ParamRecord(boolean typeVar, String name, String type, List<String> annotations) {
      super();
      this.typeVar = typeVar;
      this.name = name;
      this.type = type;
      this.annotations = annotations;
    }

    public boolean isTypeVar() {
      return typeVar;
    }

    public void setTypeVar(boolean typeVar) {
      this.typeVar = typeVar;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public List<String> getAnnotations() {
      return annotations;
    }

    public void setAnnotations(List<String> annotations) {
      this.annotations = annotations;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((type == null) ? 0 : type.hashCode());
      result = prime * result + (typeVar ? 1231 : 1237);
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ParamRecord other = (ParamRecord) obj;
      if (annotations == null) {
        if (other.annotations != null)
          return false;
      } else if (!annotations.equals(other.annotations))
        return false;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      if (type == null) {
        if (other.type != null)
          return false;
      } else if (!type.equals(other.type))
        return false;
      if (typeVar != other.typeVar)
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "ParamRecord [typeVar=" + typeVar + ", name=" + name + ", type=" + type + ", annotations=" + annotations
          + "]";
    }

    
  } /* ENDCLASS */
  
  private static class MethodRecord implements Comparable<MethodRecord> {
    
    private String              name;
    private List<ParamRecord>   params;
    private Class<?>            returnType;
    private String              returnTypeName;
    private List<String>        returnAnnotations;
    
    public MethodRecord(String name, List<ParamRecord> params, Class<?> returnType, String returnTypeName, List<String> returnAnnotations) {
      super();
      this.name = name;
      this.params = params;
      this.returnType = returnType;
      this.returnTypeName = returnTypeName;
      this.returnAnnotations = returnAnnotations;
    }
    
    
    
    @Override
    public int compareTo(MethodRecord o) {
      int result = name.compareTo(o.getName());
      if (result == 0) {
        result = Integer.compare(params.size(), o.getParams().size());
      }
      return result;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public List<ParamRecord> getParams() {
      return params;
    }

    public void setParams(List<ParamRecord> params) {
      this.params = params;
    }

    public Class<?> getReturnType() {
      return returnType;
    }

    public void setReturnType(Class<?> returnType) {
      this.returnType = returnType;
    }

    public String getReturnTypeName() {
      return returnTypeName;
    }

    public void setReturnTypeName(String returnTypeName) {
      this.returnTypeName = returnTypeName;
    }

    public List<String> getReturnAnnotations() {
      return returnAnnotations;
    }

    public void setReturnAnnotations(List<String> returnAnnotations) {
      this.returnAnnotations = returnAnnotations;
    }



    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      result = prime * result + ((params == null) ? 0 : params.hashCode());
      result = prime * result + ((returnAnnotations == null) ? 0 : returnAnnotations.hashCode());
      result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
      result = prime * result + ((returnTypeName == null) ? 0 : returnTypeName.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      MethodRecord other = (MethodRecord) obj;
      if (name == null) {
        if (other.name != null)
          return false;
      } else if (!name.equals(other.name))
        return false;
      if (params == null) {
        if (other.params != null)
          return false;
      } else if (!params.equals(other.params))
        return false;
      if (returnAnnotations == null) {
        if (other.returnAnnotations != null)
          return false;
      } else if (!returnAnnotations.equals(other.returnAnnotations))
        return false;
      if (returnType == null) {
        if (other.returnType != null)
          return false;
      } else if (!returnType.equals(other.returnType))
        return false;
      if (returnTypeName == null) {
        if (other.returnTypeName != null)
          return false;
      } else if (!returnTypeName.equals(other.returnTypeName))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "MethodRecord [name=" + name + ", params=" + params + ", returnType=" + returnType + ", returnTypeName="
          + returnTypeName + ", returnAnnotations=" + returnAnnotations + "]";
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
