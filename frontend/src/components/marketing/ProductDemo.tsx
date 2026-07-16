import productDemoVideo from '@/assets/product-demo.mp4'

export function ProductDemo() {
    return (
        <video
            autoPlay
            loop
            muted
            playsInline
            preload="metadata"
            className="block h-auto w-full"
        >
            <source src={productDemoVideo} type="video/mp4" />
        </video>
    )
}
