package com.kasisoft.libs.common.i18n;

import jakarta.validation.constraints.*;

import java.lang.annotation.*;

/**
 * A simple annotation providing a default value for a translatable constant.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18N {

  /**
   * Returns the default value for the annotated field.
   *
   * @return   The default value for the annotated field.
   */
  @NotBlank String value();

  /**
   * Returns a key which would override the field derived key name.
   *
   * @return   A key which would override the field derived key name.
   */
  @NotNull String key() default "";

} /* ENDANNOTATION */
