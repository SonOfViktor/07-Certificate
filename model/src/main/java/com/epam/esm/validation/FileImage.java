package com.epam.esm.validation;

import com.epam.esm.validation.impl.FileImageValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = FileImageValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface FileImage {

    String message() default "Only not empty png or jpg images are allowed";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
