import { useEffect } from 'react'
import { useForm } from 'react-hook-form'

import { updateEmployee } from '@/api/employeeApi'
import type { EmployeeDetailsDTO, EmployeeUpdateDTO } from '@/api/types'
import { usePositions } from '@/api/usePositions'
import {
    FloatingPanelBody,
    FloatingPanelFooter,
    FloatingPanelHeader,
    useFloatingPanel,
} from '@/components/ui/floating-panel'

type Props = {
    employee: EmployeeDetailsDTO
    onEmployeeUpdated?: () => void | Promise<void>
}

type EmployeeProfileHeaderFormValues = {
    firstName: string
    lastName: string
    positionCode: string
}

function buildEmployeeProfileUpdatePayload(
    values: EmployeeProfileHeaderFormValues,
    employee: EmployeeDetailsDTO
): EmployeeUpdateDTO {
    const payload: EmployeeUpdateDTO = {}

    if (values.firstName !== employee.firstName) {
        payload.firstName = values.firstName
    }

    if (values.lastName !== employee.lastName) {
        payload.lastName = values.lastName
    }

    if (values.positionCode !== employee.positionCode) {
        payload.positionCode = values.positionCode
    }

    return payload
}

export function EmployeeProfileHeaderEditForm({
    employee,
    onEmployeeUpdated,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const { positions, loading: positionsLoading } = usePositions()

    const form = useForm<EmployeeProfileHeaderFormValues>({
        defaultValues: {
            firstName: employee.firstName ?? '',
            lastName: employee.lastName ?? '',
            positionCode: employee.positionCode ?? '',
        },
    })

    useEffect(() => {
        if (!positionsLoading && employee.positionCode) {
            form.setValue('positionCode', employee.positionCode)
        }
    }, [employee.positionCode, form, positionsLoading])

    const isSubmitting = form.formState.isSubmitting

    async function handleSubmit(values: EmployeeProfileHeaderFormValues) {
        const payload = buildEmployeeProfileUpdatePayload(values, employee)

        if (Object.keys(payload).length === 0) {
            closeFloatingPanel()
            return
        }

        await updateEmployee(employee.publicId, payload)
        await onEmployeeUpdated?.()
        closeFloatingPanel()
    }

    return (
        <form onSubmit={form.handleSubmit(handleSubmit)}>
            <FloatingPanelHeader className="border-b border-slate-100 px-4 py-3">
                <div>
                    <div className="text-[13px] font-medium text-slate-800">
                        Edit profile
                    </div>
                    <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                        Update this employee’s name and role.
                    </p>
                </div>
            </FloatingPanelHeader>

            <FloatingPanelBody className="space-y-4 px-4 py-4">
                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">First name</span>
                    <input
                        {...form.register('firstName')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />
                </label>

                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">Last name</span>
                    <input
                        {...form.register('lastName')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />
                </label>

                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">Position</span>
                    <select
                        {...form.register('positionCode')}
                        disabled={positionsLoading}
                        className="h-9 w-full rounded-lg border border-slate-200 bg-white px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        {positions.map((position) => (
                            <option key={position.code} value={position.code}>
                                {position.displayName}
                            </option>
                        ))}
                    </select>
                </label>
            </FloatingPanelBody>

            <FloatingPanelFooter className="justify-end gap-2 border-t border-slate-100 px-4 py-3">
                <button
                    type="button"
                    onClick={closeFloatingPanel}
                    className="rounded-lg px-3 py-1.5 text-[13px] font-medium text-slate-500 hover:text-slate-800"
                >
                    Cancel
                </button>

                <button
                    type="submit"
                    disabled={isSubmitting || positionsLoading}
                    className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white hover:bg-teal-700 disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {isSubmitting ? 'Saving...' : 'Save changes'}
                </button>
            </FloatingPanelFooter>
        </form>
    )
}