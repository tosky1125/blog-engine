package com.example.blogenginekotlin.exception

open class BlogEngineException(
    message: String,
    val errorCode: String
) : RuntimeException(message)

class ResourceNotFoundException(
    resource: String,
    field: String,
    value: Any
) : BlogEngineException(
    message = "$resource not found with $field: $value",
    errorCode = "RESOURCE_NOT_FOUND"
)

class DuplicateResourceException(
    resource: String,
    field: String,
    value: Any
) : BlogEngineException(
    message = "$resource already exists with $field: $value",
    errorCode = "DUPLICATE_RESOURCE"
)

class UnauthorizedException(
    message: String = "Unauthorized access"
) : BlogEngineException(
    message = message,
    errorCode = "UNAUTHORIZED"
)

class ForbiddenException(
    message: String = "Access forbidden"
) : BlogEngineException(
    message = message,
    errorCode = "FORBIDDEN"
)

class ValidationException(
    message: String
) : BlogEngineException(
    message = message,
    errorCode = "VALIDATION_ERROR"
)

class BusinessLogicException(
    message: String,
    errorCode: String = "BUSINESS_LOGIC_ERROR"
) : BlogEngineException(
    message = message,
    errorCode = errorCode
)