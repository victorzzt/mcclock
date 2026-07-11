package com.victorzzt.mcclock

/**
 * 用户可见 Widget 变体的集中注册表。
 *
 * 每个枚举项只声明渲染模式和素材；Provider 生命周期与共享布局保持统一。新增变体时应在此处
 * 选择 [ClockRenderConfig]，并由清单中的薄 Provider 类绑定到对应枚举项。
 */
enum class WidgetVariant(
    val renderConfig: ClockRenderConfig,
    val layoutResId: Int = R.layout.widget_clock
) {
    CLASSIC(
        renderConfig = ClockRenderConfig.Procedural(
            maskResId = R.drawable.clock_mask_classic,
            dialResId = R.drawable.clock_dial_classic
        )
    ),
    CLASSIC_HI_RES(
        renderConfig = ClockRenderConfig.Procedural(
            maskResId = R.drawable.clock_mask_classic,
            dialResId = R.drawable.clock_dial_classic,
            upscaleSizePx = HIGH_RES_SIZE_PX
        )
    ),
    CLASSIC_MOON_PHASE(
        renderConfig = ClockRenderConfig.MoonPhase(
            maskResId = R.drawable.clock_mask_classic,
            dialResources = MoonPhaseDialResources.classic
        )
    ),
    CLASSIC_15(
        renderConfig = ClockRenderConfig.SpriteSheet(
            spriteSheetResId = R.drawable.clock_15
        )
    ),
    MODERN(
        renderConfig = ClockRenderConfig.SpriteSheet(
            spriteSheetResId = R.drawable.clock_17
        )
    ),
    MODERN_RENDERED(
        renderConfig = ClockRenderConfig.Procedural(
            maskResId = R.drawable.clock_mask_modern,
            dialResId = R.drawable.clock_dial_modern
        )
    ),
    MODERN_HI_RES(
        renderConfig = ClockRenderConfig.Procedural(
            maskResId = R.drawable.clock_mask_modern,
            dialResId = R.drawable.clock_dial_modern,
            upscaleSizePx = HIGH_RES_SIZE_PX
        )
    ),
    MODERN_MOON_PHASE(
        renderConfig = ClockRenderConfig.MoonPhase(
            maskResId = R.drawable.clock_mask_modern,
            dialResources = MoonPhaseDialResources.modern
        )
    );

    /** 用该变体的静态配置创建渲染器，并允许测试替换资源加载器。 */
    internal fun createRenderer(
        bitmapLoader: BitmapResourceLoader = CachedBitmapResourceLoader
    ): ClockRenderer = renderConfig.createRenderer(bitmapLoader)
}

/**
 * 构造 [ClockRenderer] 所需的不可变配置，不包含生命周期状态或已解码位图。
 *
 * 将配置与具体渲染器分离，可在不合并各变体视觉行为的前提下集中维护资源选择。
 */
sealed interface ClockRenderConfig {
    /** 由固定遮罩和表盘纹理按时间角度合成的渲染配置。 */
    data class Procedural(
        val maskResId: Int,
        val dialResId: Int,
        val upscaleSizePx: Int? = null
    ) : ClockRenderConfig

    /** 由固定遮罩和现实月相表盘合成的渲染配置。 */
    data class MoonPhase(
        val maskResId: Int,
        val dialResources: MoonPhaseDialResources,
        val upscaleSizePx: Int? = HIGH_RES_SIZE_PX
    ) : ClockRenderConfig

    /** 从纵向精灵表直接截取离散时间帧的渲染配置。 */
    data class SpriteSheet(
        val spriteSheetResId: Int,
        val frameWidth: Int = 16,
        val frameHeight: Int = 16,
        val frameCount: Int = ClockTime.DEFAULT_SPRITE_FRAME_COUNT,
        val midnightFrameOffset: Int = ClockTime.DEFAULT_MIDNIGHT_FRAME_OFFSET
    ) : ClockRenderConfig

    /** 把声明式配置实例化为对应的无状态渲染策略。 */
    fun createRenderer(bitmapLoader: BitmapResourceLoader): ClockRenderer = when (this) {
        is Procedural -> ProceduralClockRenderer(
            maskResId = maskResId,
            dialResId = dialResId,
            upscaleSizePx = upscaleSizePx,
            bitmapLoader = bitmapLoader
        )

        is MoonPhase -> MoonPhaseClockRenderer(
            maskResId = maskResId,
            dialResources = dialResources,
            upscaleSizePx = upscaleSizePx,
            bitmapLoader = bitmapLoader
        )

        is SpriteSheet -> SpriteSheetClockRenderer(
            spriteSheetResId = spriteSheetResId,
            frameWidth = frameWidth,
            frameHeight = frameHeight,
            frameCount = frameCount,
            midnightFrameOffset = midnightFrameOffset,
            bitmapLoader = bitmapLoader
        )
    }
}
