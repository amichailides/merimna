import type {
    FieldValues,
    Path,
    UseFormReturn,
} from 'react-hook-form'

type ValidationErrors = Record<string, string[]>

type ApplyServerValidationErrorsOptions<
    TFieldValues extends FieldValues,
    TTransformedValues extends FieldValues = TFieldValues,
> = {
    form: UseFormReturn<TFieldValues, unknown, TTransformedValues>
    validationErrors: ValidationErrors
    isFormPath: (path: string) => path is Path<TFieldValues>
}

export function applyServerValidationErrors<
    TFieldValues extends FieldValues,
    TTransformedValues extends FieldValues = TFieldValues,
>({
    form,
    validationErrors,
    isFormPath,
}: ApplyServerValidationErrorsOptions<
    TFieldValues,
    TTransformedValues
>): {
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
