import axios from 'axios'
import { BriefcaseBusiness, History, MoreHorizontal } from 'lucide-react'
import { useState } from 'react'

import {
    reactivateEmployee,
    terminateEmployee,
} from '@/api/employeeApi'
import type { EmployeeDetailsDTO } from '@/api/types'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
    FloatingPanelContent,
    FloatingPanelRoot,
} from '@/components/ui/floating-panel'

type Props = {
    employee: EmployeeDetailsDTO
    onViewActivity?: () => void
    onManageAssignments?: () => void
    onEmployeeUpdated?: () => void | Promise<void>
}

export function EmployeeProfileActionsMenu({
    employee,
    onViewActivity,
    onManageAssignments,
    onEmployeeUpdated,
}: Props) {
    const [isDeactivateOpen, setIsDeactivateOpen] = useState(false)
    const [isReactivateOpen, setIsReactivateOpen] = useState(false)

    const today = new Date().toISOString().slice(0, 10)

    const [terminationDate, setTerminationDate] = useState(today)

    const [isDeactivating, setIsDeactivating] = useState(false)
    const [isReactivating, setIsReactivating] = useState(false)

    const [deactivateError, setDeactivateError] = useState<string | null>(
        null
    )
    const [reactivateError, setReactivateError] = useState<string | null>(
        null
    )

    const terminationDateError =
        !terminationDate
            ? 'Termination date is required'
            : terminationDate > today
                ? 'Termination date cannot be in the future'
                : null

    async function handleDeactivate() {
        if (terminationDateError) {
            return
        }

        setDeactivateError(null)
        setIsDeactivating(true)

        try {
            await terminateEmployee(employee.publicId, {
                terminationDate,
            })

            setIsDeactivateOpen(false)
            await onEmployeeUpdated?.()
        } catch (error) {
            const detail = axios.isAxiosError<{ detail?: string }>(error)
                ? error.response?.data?.detail
                : undefined

            setDeactivateError(
                detail ??
                    'Could not deactivate the employee. Please try again.'
            )
        } finally {
            setIsDeactivating(false)
        }
    }

    async function handleReactivate() {
        setReactivateError(null)
        setIsReactivating(true)

        try {
            await reactivateEmployee(employee.publicId)

            setIsReactivateOpen(false)
            await onEmployeeUpdated?.()
        } catch (error) {
            const detail = axios.isAxiosError<{ detail?: string }>(error)
                ? error.response?.data?.detail
                : undefined

            setReactivateError(
                detail ??
                    'Could not reactivate the employee. Please try again.'
            )
        } finally {
            setIsReactivating(false)
        }
    }

    return (
        <>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                    <button
                        type="button"
                        className="inline-flex h-9 w-9 items-center justify-center rounded-lg border border-slate-200 bg-white text-slate-500 transition-colors hover:border-slate-300 hover:bg-slate-50 hover:text-slate-950"
                        aria-label="Employee actions"
                    >
                        <MoreHorizontal
                            size={16}
                            strokeWidth={2.25}
                        />
                    </button>
                </DropdownMenuTrigger>

                <DropdownMenuContent
                    align="end"
                    sideOffset={8}
                    className="w-47 rounded-xl border border-slate-200 bg-white p-1.5 shadow-sm ring-0"
                >
                    <DropdownMenuItem
                        onSelect={onViewActivity}
                        className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal text-slate-600 focus:bg-slate-50 focus:text-slate-800"
                    >
                        <History
                            size={14}
                            strokeWidth={2}
                            className="text-slate-400"
                        />
                        View activity
                    </DropdownMenuItem>

                    <DropdownMenuItem
                        onSelect={onManageAssignments}
                        className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal text-slate-600 focus:bg-slate-50 focus:text-slate-800"
                    >
                        <BriefcaseBusiness
                            size={14}
                            strokeWidth={2}
                            className="text-slate-400"
                        />
                        Manage assignments
                    </DropdownMenuItem>

                    <DropdownMenuSeparator className="my-1 bg-slate-100" />

                    {employee.active ? (
                        <DropdownMenuItem
                            variant="destructive"
                            className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal data-[variant=destructive]:focus:bg-red-50/60 data-[variant=destructive]:text-red-500 data-[variant=destructive]:focus:text-red-500"
                            onSelect={(event) => {
                                event.preventDefault()
                                setDeactivateError(null)
                                setIsDeactivateOpen(true)
                            }}
                        >
                            Deactivate employee
                        </DropdownMenuItem>
                    ) : (
                        <DropdownMenuItem
                            className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal text-slate-700 focus:bg-slate-50"
                            onSelect={(event) => {
                                event.preventDefault()
                                setReactivateError(null)
                                setIsReactivateOpen(true)
                            }}
                        >
                            Reactivate employee
                        </DropdownMenuItem>
                    )}
                </DropdownMenuContent>
            </DropdownMenu>

            <FloatingPanelRoot
                open={isDeactivateOpen}
                onOpenChange={(open) => {
                    setIsDeactivateOpen(open)

                    if (!open) {
                        setDeactivateError(null)
                    }
                }}
            >
                <FloatingPanelContent
                    align="center"
                    className="w-[360px] rounded-xl border border-slate-200 bg-white shadow-sm"
                >
                    <form
                        className="space-y-4 p-4"
                        onSubmit={(event) => {
                            event.preventDefault()
                            void handleDeactivate()
                        }}
                    >
                        <div className="space-y-1.5">
                            <label
                                htmlFor="termination-date"
                                className="text-[12px] text-slate-500"
                            >
                                Termination date
                            </label>

                            <input
                                id="termination-date"
                                type="date"
                                max={today}
                                value={terminationDate}
                                onChange={(event) =>
                                    setTerminationDate(
                                        event.target.value
                                    )
                                }
                                className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-900 outline-none transition-colors focus:border-slate-300"
                            />

                            {terminationDateError && (
                                <p className="text-[11px] text-red-500">
                                    {terminationDateError}
                                </p>
                            )}
                        </div>

                        {deactivateError && (
                            <p
                                role="alert"
                                className="text-[12px] text-red-500"
                            >
                                {deactivateError}
                            </p>
                        )}

                        <div className="flex justify-end gap-2 pt-1">
                            <button
                                type="button"
                                disabled={isDeactivating}
                                onClick={() =>
                                    setIsDeactivateOpen(false)
                                }
                                className="rounded-lg border border-slate-200 px-3 py-1.5 text-[13px] font-medium text-slate-700 transition-colors hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                Cancel
                            </button>

                            <button
                                type="submit"
                                disabled={
                                    isDeactivating ||
                                    !!terminationDateError
                                }
                                className="rounded-lg bg-red-500 px-3 py-1.5 text-[13px] font-medium text-white transition-colors hover:bg-red-600 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                {isDeactivating
                                    ? 'Deactivating...'
                                    : 'Deactivate employee'}
                            </button>
                        </div>
                    </form>
                </FloatingPanelContent>
            </FloatingPanelRoot>

            <FloatingPanelRoot
                open={isReactivateOpen}
                onOpenChange={(open) => {
                    setIsReactivateOpen(open)

                    if (!open) {
                        setReactivateError(null)
                    }
                }}
            >
                <FloatingPanelContent
                    align="center"
                    className="w-[360px] rounded-xl border border-slate-200 bg-white shadow-sm"
                >
                    <div className="space-y-4 p-4">
                        <div className="space-y-1">
                            <h2 className="text-[13px] font-medium text-slate-900">
                                Reactivate employee
                            </h2>

                            <p className="text-[13px] text-slate-500">
                                This will mark {employee.firstName}{' '}
                                {employee.lastName} as active again.
                            </p>
                        </div>

                        {reactivateError && (
                            <p
                                role="alert"
                                className="text-[12px] text-red-500"
                            >
                                {reactivateError}
                            </p>
                        )}

                        <div className="flex justify-end gap-2 pt-1">
                            <button
                                type="button"
                                disabled={isReactivating}
                                onClick={() =>
                                    setIsReactivateOpen(false)
                                }
                                className="rounded-lg border border-slate-200 px-3 py-1.5 text-[13px] font-medium text-slate-700 transition-colors hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                Cancel
                            </button>

                            <button
                                type="button"
                                disabled={isReactivating}
                                onClick={() =>
                                    void handleReactivate()
                                }
                                className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white transition-colors hover:bg-teal-700 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                {isReactivating
                                    ? 'Reactivating...'
                                    : 'Reactivate employee'}
                            </button>
                        </div>
                    </div>
                </FloatingPanelContent>
            </FloatingPanelRoot>
        </>
    )
}
