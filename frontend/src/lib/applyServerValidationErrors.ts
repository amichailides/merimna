import type {
    FieldValues,
    Path,
    UseFormReturn,
} from 'react-hook-form'

type ValidationErrors = Record<string, string[]>

type ApplyServerValidationErrorsOptions<T extends FieldValues> = {
    form: UseFormReturn<T>
    validationErrors: ValidationErrors
    isFormPath: (path: string) => path is Path<T>
}

export function applyServerValidationErrors<T extends FieldValues>({
    form,
    validationErrors,
    isFormPath,
}: ApplyServerValidationErrorsOptions<T>): {
    fieldErrorApplied: boolean
    unknownFieldError: boolean
} {
    let fieldErrorApplied = false
    let unknownFieldError = false

    for (const [path, messages] of Object.entries(validationErrors)) {
        const message = messages[0]

        if (!message) {
            continue
        }

        if (!isFormPath(path)) {
            unknownFieldError = true
            continue
        }

        form.setError(path, {
            type: 'server',
            message,
        })

        fieldErrorApplied = true
    }

    return {
        fieldErrorApplied,
        unknownFieldError,
    }
}