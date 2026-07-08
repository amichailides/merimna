import { useForm } from 'react-hook-form'

import type { EmployeeDetailsDTO, EmployeeUpdateDTO } from '@/api/types'
import { updateEmployee } from '@/api/employeeApi'
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

type EmployeeMetadataFormValues = {
    contactEmail: string
    mobileNumber: string
    address: {
        street: string
        streetNumber: string
        city: string
        zipCode: string
    }
    emergencyContactName: string
    emergencyContactPhoneNumber: string
    hireDate: string
    dateOfBirth: string
}

export function EmployeeMetadataEditForm({
    employee,
    onEmployeeUpdated,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const form = useForm<EmployeeMetadataFormValues>({
        defaultValues: {
            contactEmail: employee.contactEmail ?? '',
            mobileNumber: employee.mobileNumber ?? '',
            address: {
                street: employee.address?.street ?? '',
                streetNumber: employee.address?.streetNumber ?? '',
                city: employee.address?.city ?? '',
                zipCode: employee.address?.zipCode ?? '',
            },
            emergencyContactName: employee.emergencyContactName ?? '',
            emergencyContactPhoneNumber: employee.emergencyContactPhoneNumber ?? '',
            hireDate: employee.hireDate ?? '',
            dateOfBirth: employee.dateOfBirth ?? '',
        },
    })

    const isSubmitting = form.formState.isSubmitting

    async function handleSubmit(values: EmployeeMetadataFormValues) {
        const payload: EmployeeUpdateDTO = {
            contactEmail: values.contactEmail,
            mobileNumber: values.mobileNumber,
            address: values.address,
            emergencyContactName: values.emergencyContactName,
            emergencyContactPhoneNumber: values.emergencyContactPhoneNumber,
            hireDate: values.hireDate,
            dateOfBirth: values.dateOfBirth,
        }

        await updateEmployee(employee.publicId, payload)
        await onEmployeeUpdated?.()
    }

    return (
        <form
            className="min-h-[520px]"
            onSubmit={form.handleSubmit(handleSubmit)}
        >
            <FloatingPanelHeader className="border-b border-slate-100 px-4 py-3">
                <div>
                    <div className="text-[13px] font-medium text-slate-800">
                        Edit employee details
                    </div>
                    <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                        Update this employee’s profile information.
                    </p>
                </div>
            </FloatingPanelHeader>

            <FloatingPanelBody className="space-y-5 px-4 py-4">
                <section className="space-y-3">
                    <h3 className="text-[12px] font-medium text-slate-700">
                        Contact
                    </h3>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Email</span>
                        <input
                            {...form.register('contactEmail')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Mobile</span>
                        <input
                            {...form.register('mobileNumber')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>

                    <div className="space-y-2">
                        <div className="text-[11px] text-slate-400">
                            Address
                        </div>

                        <div className="grid grid-cols-2 gap-2">
                            <label className="block space-y-1.5">
                                <span className="text-[11px] text-slate-400">Street</span>
                                <input
                                    {...form.register('address.street')}
                                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                />
                            </label>

                            <label className="block space-y-1.5">
                                <span className="text-[11px] text-slate-400">No.</span>
                                <input
                                    {...form.register('address.streetNumber')}
                                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                />
                            </label>

                            <label className="block space-y-1.5">
                                <span className="text-[11px] text-slate-400">City</span>
                                <input
                                    {...form.register('address.city')}
                                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                />
                            </label>

                            <label className="block space-y-1.5">
                                <span className="text-[11px] text-slate-400">ZIP</span>
                                <input
                                    {...form.register('address.zipCode')}
                                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                />
                            </label>
                        </div>
                    </div>
                </section>

                <section className="space-y-3 border-t border-slate-100 pt-4">
                    <h3 className="text-[12px] font-medium text-slate-700">
                        Emergency contact
                    </h3>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Name</span>
                        <input
                            {...form.register('emergencyContactName')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Phone</span>
                        <input
                            {...form.register('emergencyContactPhoneNumber')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>
                </section>

                <section className="space-y-3 border-t border-slate-100 pt-4">
                    <h3 className="text-[12px] font-medium text-slate-700">
                        Employment
                    </h3>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Hire date</span>
                        <input
                            type="date"
                            {...form.register('hireDate')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">Date of birth</span>
                        <input
                            type="date"
                            {...form.register('dateOfBirth')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />
                    </label>
                </section>
            </FloatingPanelBody>

            <FloatingPanelFooter className="mt-auto justify-end gap-2 border-t border-slate-100 px-4 py-3">
                <button
                    type="button"
                    onClick={closeFloatingPanel}
                    className="rounded-lg px-3 py-1.5 text-[13px] font-medium text-slate-500 hover:text-slate-800"
                >
                    Cancel
                </button>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white hover:bg-teal-700 disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {isSubmitting ? 'Saving...' : 'Save changes'}
                </button>
            </FloatingPanelFooter>
        </form>
    )
}