import { ArrowLeft } from 'lucide-react'
import { Link } from 'react-router-dom'

export function AssignmentsPage() {
    return (
        <main className="max-w-3xl space-y-4">
            <section>
                <h1 className="text-[18px] font-medium text-slate-900">
                    Assignments
                </h1>

                <p className="mt-0.5 text-[13px] text-slate-400">
                    Assignment management is not available in the frontend yet.
                </p>
            </section>

            <Link
                to="/dashboard"
                className="inline-flex items-center gap-1.5 text-[12px] font-medium text-teal-700 hover:text-teal-800"
            >
                <ArrowLeft className="h-3.5 w-3.5" />
                Back to dashboard
            </Link>
        </main>
    )
}
