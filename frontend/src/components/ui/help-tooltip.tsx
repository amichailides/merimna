import { HelpCircle } from 'lucide-react'

import {
    Tooltip,
    TooltipContent,
    TooltipTrigger,
} from '@/components/ui/tooltip'

type HelpTooltipProps = {
    content: string
}

export function HelpTooltip({ content }: HelpTooltipProps) {
    return (
        <Tooltip>
            <TooltipTrigger asChild>
                <button
                    type="button"
                    className="inline-flex h-4 w-4 items-center justify-center rounded-full text-slate-400 transition-colors hover:text-slate-600"
                    aria-label="More information"
                >
                    <HelpCircle size={13} />
                </button>
            </TooltipTrigger>

            <TooltipContent
                side="top"
                align="start"
                className="max-w-xs text-xs leading-5"
            >
                {content}
            </TooltipContent>
        </Tooltip>
    )
}