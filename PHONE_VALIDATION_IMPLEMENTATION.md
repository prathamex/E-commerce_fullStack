# Phone Number Validation Implementation

## Overview
This document describes the phone number validation implementation for the Shop Spring Boot backend, particularly for user registration.

## Changes Made

### 1. ValidMobileNumberValidator.java
**Location:** `src/main/java/com/cws/shop/validation/ValidMobileNumberValidator.java`

**Updates:**
- Enhanced the validator to provide specific error messages for different validation scenarios
- Maintains the distinction between alphabetic characters and special characters

**Error Messages:**
- **Alphabetic Characters:** "Phone number cannot contain letters. Please enter a valid phone number."
- **Special Characters/Non-numeric:** "Phone number cannot contain special characters. Please enter a valid phone number."
- **Invalid Length:** "Please enter a valid 10-digit mobile number."

### 2. ValidMobileNumber.java
**Location:** `src/main/java/com/cws/shop/validation/ValidMobileNumber.java`

**Updates:**
- Updated the default message to: "Please enter a valid 10-digit mobile number."

### 3. ValidMobileNumberValidatorTest.java
**Location:** `src/test/java/com/cws/shop/validation/ValidMobileNumberValidatorTest.java`

**Updates:**
- Updated test assertions to verify the new, specific error messages
- All 4 tests pass successfully

## Validation Flow

### In CreateUserRequestDto
The registration DTO uses the following validations:

```java
@NotBlank(message = "Mobile Number is required")
@ValidMobileNumber
private String mobileNumber;
```

### Validation Hierarchy
1. **@NotBlank** - Handles empty/null phone numbers
   - Error: "Mobile Number is required"

2. **@ValidMobileNumber** - Custom validation with multiple rules:
   - Checks for alphabetic characters
   - Checks for special characters (non-digit characters)
   - Validates length (must be exactly 10 digits)

## Test Coverage

All validation scenarios are covered with test cases:

| Test Case | Input | Expected Error |
|-----------|-------|-----------------|
| Empty | Empty string | "Mobile Number is required" (@NotBlank) |
| With Letters | "12345abcde" | "Phone number cannot contain letters. Please enter a valid phone number." |
| With Symbols | "12345-6789" | "Phone number cannot contain special characters. Please enter a valid phone number." |
| Wrong Length | "12345" | "Please enter a valid 10-digit mobile number." |
| Valid | "9876543210" | No error |

## Build Status
✅ All tests passing  
✅ No compilation errors  
✅ Compatible with Jakarta Validation API

## Usage

When a user registers with invalid phone number data:

1. **Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "asdfghjk",
  "password": "password123"
}
```

2. **Response (ValidationException):**
```json
{
  "mobileNumber": "Phone number cannot contain letters. Please enter a valid phone number."
}
```

## Implementation Details

### Validator Logic Flow
```
1. Check if value is null or empty → return true (let @NotBlank handle)
2. Check for letters → if found, return specific error
3. Check for non-digit characters → if found, return specific error
4. Check length (must be exactly 10) → if invalid, return specific error
5. If all checks pass → return true
```

### Why This Approach
- **Clear User Feedback:** Users get specific, actionable error messages
- **Progressive Validation:** Checks are performed in order of relevance
- **Jakarta Validation Compliant:** Uses standard Bean Validation API
- **Backward Compatible:** Existing registration functionality remains unchanged
- **Test Coverage:** All edge cases are covered with unit tests

## Notes
- The validator respects null/empty values, allowing @NotBlank to handle those cases
- Phone numbers are trimmed before validation
- The validation works for all use cases where @ValidMobileNumber is applied
- Currently applied to the `mobileNumber` field in `CreateUserRequestDto`
