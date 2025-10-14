package com.whatsyourpick.ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * 폰트 로딩 및 관리를 담당하는 클래스
 */
public class FontManager {

    private static Font pressStart2P;
    private static Font dungGeunMo;

    /**
     * 폰트를 초기화합니다.
     */
    public static void initialize() {
        try {
            // Press Start 2P 폰트 로드
            InputStream ps2pStream = FontManager.class.getResourceAsStream("/fonts/PressStart2P-Regular.ttf");
            if (ps2pStream != null) {
                pressStart2P = Font.createFont(Font.TRUETYPE_FONT, ps2pStream);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(pressStart2P);
                ps2pStream.close();
            } else {
                System.err.println("Press Start 2P 폰트 파일을 찾을 수 없습니다.");
                pressStart2P = new Font("Arial", Font.BOLD, 12);
            }

            // 둥근모 폰트 로드
            InputStream dgmStream = FontManager.class.getResourceAsStream("/fonts/DungGeunMo.ttf");
            if (dgmStream != null) {
                dungGeunMo = Font.createFont(Font.TRUETYPE_FONT, dgmStream);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(dungGeunMo);
                dgmStream.close();
            } else {
                System.err.println("둥근모 폰트 파일을 찾을 수 없습니다.");
                dungGeunMo = new Font("맑은 고딕", Font.PLAIN, 12);
            }

        } catch (FontFormatException | IOException e) {
            System.err.println("폰트 로딩 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            // 폴백 폰트 설정
            pressStart2P = new Font("Arial", Font.BOLD, 12);
            dungGeunMo = new Font("맑은 고딕", Font.PLAIN, 12);
        }
    }

    /**
     * Press Start 2P 폰트를 반환합니다. (영어 타이틀/버튼용)
     * @param size 폰트 크기
     * @return Press Start 2P 폰트
     */
    public static Font getPressStart2P(float size) {
        if (pressStart2P == null) {
            initialize();
        }
        return pressStart2P.deriveFont(size);
    }

    /**
     * 둥근모 폰트를 반환합니다. (한글 텍스트용)
     * @param size 폰트 크기
     * @return 둥근모 폰트
     */
    public static Font getDungGeunMo(float size) {
        if (dungGeunMo == null) {
            initialize();
        }
        return dungGeunMo.deriveFont(size);
    }

    /**
     * Press Start 2P 폰트를 스타일과 함께 반환합니다.
     * @param style 폰트 스타일 (Font.PLAIN, Font.BOLD 등)
     * @param size 폰트 크기
     * @return Press Start 2P 폰트
     */
    public static Font getPressStart2P(int style, float size) {
        if (pressStart2P == null) {
            initialize();
        }
        return pressStart2P.deriveFont(style, size);
    }

    /**
     * 둥근모 폰트를 스타일과 함께 반환합니다.
     * @param style 폰트 스타일 (Font.PLAIN, Font.BOLD 등)
     * @param size 폰트 크기
     * @return 둥근모 폰트
     */
    public static Font getDungGeunMo(int style, float size) {
        if (dungGeunMo == null) {
            initialize();
        }
        return dungGeunMo.deriveFont(style, size);
    }
}