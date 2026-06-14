import { ChevronLeftIcon, ChevronRightIcon } from 'lucide-react'

import { Button } from '@/components/ui/button'
import { Label } from '@/components/ui/label'
import {
    Pagination,
    PaginationContent,
    PaginationItem,
} from '@/components/ui/pagination'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

type ListPaginationProps = {
    page: number
    size: number
    totalElements: number
    totalPages: number
    onPaginationChange: (page: number, size: number) => void
}

export function ListPagination({
    page,
    size,
    totalElements,
    totalPages,
    onPaginationChange,
}: ListPaginationProps) {
    const startItem = totalElements === 0 ? 0 : page * size + 1
    const endItem = Math.min((page + 1) * size, totalElements)

    const isFirstPage = page === 0
    const isLastPage = page >= totalPages - 1 || totalPages === 0

    return (
        <div className="flex w-full flex-col gap-3 rounded-lg border bg-white p-4 sm:flex-row sm:items-center sm:justify-between">
            <div className="flex items-center gap-2">
                <Label className="whitespace-nowrap text-sm">Rows per page</Label>

                <Select
                    value={size.toString()}
                    onValueChange={(value) => onPaginationChange(0, Number(value))}
                >
                    <SelectTrigger className="h-9 w-20">
                        <SelectValue />
                    </SelectTrigger>

                    <SelectContent>
                        <SelectItem value="10">10</SelectItem>
                        <SelectItem value="20">20</SelectItem>
                        <SelectItem value="50">50</SelectItem>
                    </SelectContent>
                </Select>
            </div>

            <div className="flex items-center justify-between gap-3 sm:justify-end">
                <span className="whitespace-nowrap text-sm text-slate-500">
                    {startItem}-{endItem} of {totalElements}
                </span>

                <Pagination className="w-auto">
                    <PaginationContent>
                        <PaginationItem>
                            <Button
                                type="button"
                                aria-label="Go to previous page"
                                variant="ghost"
                                size="icon"
                                disabled={isFirstPage}
                                onClick={() => onPaginationChange(page - 1, size)}
                            >
                                <ChevronLeftIcon className="h-4 w-4" />
                            </Button>
                        </PaginationItem>

                        <PaginationItem>
                            <Button
                                type="button"
                                aria-label="Go to next page"
                                variant="ghost"
                                size="icon"
                                disabled={isLastPage}
                                onClick={() => onPaginationChange(page + 1, size)}
                            >
                                <ChevronRightIcon className="h-4 w-4" />
                            </Button>
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            </div>
        </div>
    )
}