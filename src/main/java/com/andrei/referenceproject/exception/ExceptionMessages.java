package com.andrei.referenceproject.exception;

public final class ExceptionMessages {
    public static final String INVALID_PRIORITY_FIELDS_MESSAGE = "Priority construct fail : invalid values entered";
    public static final String INVALID_TODO_FIELDS_MESSAGE = "Todo construct fail: invalid values entered";
    public static final String TODO_NOT_FOUND_MESSAGE = "Todo with id=%d not found";
    public static final String PRIORITY_NOT_FOUND_MESSAGE = "Priority with id=%d not found";
    public static final String TODO_EXISTED_NAME_VALUES_MESSAGE = "Todo name to save is not unique";
    public static final String PRIORITY_EXISTED_VALUES_MESSAGE = "Priority fields to save are not unique";
    public static final String DELETE_BEING_USED_PRIORITY_MESSAGE = "There is at least one Todo that uses this priority";
    public static final String DEFAULT_ERROR_MESSAGE = "Program execution error";
    public static final String SELECTED_ELEMENT_HAS_BEEN_REMOVED = "Selected element has been removed";
    public static final String SELECTED_PRIORITY_IN_TODO_HAS_BEEN_REMOVED = "Selected priority in todo has been removed";
}
