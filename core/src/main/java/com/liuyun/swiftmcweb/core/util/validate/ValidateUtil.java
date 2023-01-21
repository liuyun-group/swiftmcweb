package com.liuyun.swiftmcweb.core.util.validate;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;

import javax.validation.Validation;

/**
 * @author flyan
 * @version 1.0
 * @date 10/31/22
 */
@UtilityClass
public class ValidateUtil {

    /**
     * entity validate by groups.
     *
     * @param t
     * @param groups
     * @param <T>
     */
    public static <T> ValidateResult validate(T t, Class<?>... groups) {
        var cs = Validation.buildDefaultValidatorFactory().getValidator().validate(t, groups);
        if(cs.size() > 0) {
            var error = new StringBuilder();
            for (var c : cs) {
                error.append(c.getMessage()).append(";\n");
            }
            return ValidateResult.fail(error.toString());
        }
        return ValidateResult.success();
    }

    @AllArgsConstructor
    public static class ValidateResult {
        public boolean success;
        public String errorMsg;

        static ValidateResult success() {
            return new ValidateResult(true, null);
        }

        static ValidateResult fail(String errorMsg) {
            return new ValidateResult(false, errorMsg);
        }
    }

}
