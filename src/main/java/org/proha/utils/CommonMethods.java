package org.proha.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class CommonMethods {

    public static <T, R> Optional<R> mapToDto(Optional<T> entity, Function<T, R> mapper) {
        return entity.map(mapper);
    }
    public static UUID getUUIDParameter(HttpServletRequest request, String paramName) {
        String paramValue = request.getParameter(paramName);
        return (paramValue != null && !paramValue.isEmpty()) ? UUID.fromString(paramValue) : null;
    }

    public static int getPageParameter(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        return (pageParam != null) ? Integer.parseInt(pageParam) : 1;
    }

    public static int getPageSizeParameter(HttpServletRequest request) {
        String sizeParam = request.getParameter("size");
        return (sizeParam != null) ? Integer.parseInt(sizeParam) : 50;
    }
}
