import { SearchIcon } from 'lucide-react'

import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

export function EmployeeListFilters() {
    return (
        <div className="flex flex-col gap-3 rounded-lg border bg-white p-4 sm:flex-row sm:items-center">
            <div className="relative flex-1">
                <SearchIcon className="pointer-events-none absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />

                <Input
                    type="search"
                    placeholder="Search employees..."
                    className="pl-9"
                    disabled
                />
            </div>

            <Select disabled defaultValue="all">
                <SelectTrigger className="w-full sm:w-40">
                    <SelectValue placeholder="Status" />
                </SelectTrigger>

                <SelectContent>
                    <SelectItem value="all">All</SelectItem>
                    <SelectItem value="active">Active</SelectItem>
                    <SelectItem value="inactive">Inactive</SelectItem>
                </SelectContent>
            </Select>
        </div>
    )
}