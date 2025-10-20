package com.whatsyourpick.ui;

import com.whatsyourpick.model.Contestant;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 대결 화면 패널
 * 두 대상을 표시하고 사용자가 선택할 수 있도록 합니다.
 */
public class BattlePanel extends JPanel {

    private JLabel roundInfoLabel;
    private JLabel matchCountLabel;
    private JPanel leftContestantPanel;
    private JPanel rightContestantPanel;
    private JLabel leftImageLabel;
    private JLabel rightImageLabel;
    private JLabel leftNameLabel;
    private JLabel rightNameLabel;
    private Consumer<Contestant> winnerSelectListener;
    private Contestant leftContestant;
    private Contestant rightContestant;
    private Runnable backButtonListener; // 헤더 클릭 시 돌아가기 위한 리스너 추가

    // 배경 및 색상 변수 추가 (CategoryPanel과 동일)
    private BufferedImage backgroundImage;
    private static final Color PINK_COLOR = new Color(241, 113, 151); // #F17197
    private static final Color HEADER_BG_COLOR = new Color(255, 209, 233); // #FFD1E9

    public BattlePanel() {
        // 배경 이미지 로드 로직 추가
        loadBackgroundImage();
        setLayout(new BorderLayout());
        // setBackground 대신 paintComponent에서 배경을 처리하므로, 기본 배경색은 유지보수 차원에서 제거하거나 transparent하게 둠
        // setBackground(new Color(245, 245, 250));
        initComponents();
    }

    // 배경 이미지 로드 메서드 추가
    private void loadBackgroundImage() {
        try {
            java.net.URL imageUrl = getClass().getResource("/images/background.png");

            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
            } else {
                backgroundImage = null;
                System.err.println("오류: 배경 이미지를 찾을 수 없습니다. 경로: /images/background.png");
            }
        } catch (Exception e) {
            backgroundImage = null;
            System.err.println("배경 이미지 로드 중 상세 오류: " + e.getMessage());
        }
    }

    // paintComponent 메서드 추가 (배경 이미지 그리기)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(255, 243, 253)); // #FFF3FD (배경 이미지 없을 시 대체 색상)
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initComponents() {
        // 1. 상단 헤더 패널 (♥️ Pick Me) - CategoryPanel과 동일하게 추가
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setBackground(HEADER_BG_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 3, 0, PINK_COLOR));
        headerPanel.setOpaque(true);
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel headerLabel = new JLabel("\u2665\ufe0f  Pick Me");
        headerLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 32f));
        headerLabel.setForeground(PINK_COLOR);
        headerPanel.add(headerLabel);

        // 헤더 클릭 이벤트 (뒤로가기 기능)
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonListener != null) {
                    backButtonListener.run();
                }
            }
        });

        add(headerPanel, BorderLayout.NORTH);

        // 2. 상단 정보 패널 (기존 코드)
        JPanel topInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        // 배경 이미지 위에 올라가므로 투명하게 설정
        topInfoPanel.setOpaque(false);

        roundInfoLabel = new JLabel("");
        roundInfoLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 28f));
        roundInfoLabel.setForeground(new Color(75, 0, 130));

        matchCountLabel = new JLabel("");
        matchCountLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 22f));
        matchCountLabel.setForeground(new Color(100, 100, 100));

        topInfoPanel.add(roundInfoLabel);
        topInfoPanel.add(matchCountLabel);
        // Header 아래에 TopInfoPanel을 배치하기 위해 새로운 Panel에 담아 CENTER의 NORTH에 배치
        JPanel centerNorthContainer = new JPanel(new BorderLayout());
        centerNorthContainer.setOpaque(false);
        centerNorthContainer.add(topInfoPanel, BorderLayout.NORTH);

        // 3. 중앙 대결 패널 (기존 코드)
        JPanel battlePanel = new JPanel(new GridBagLayout());
        // 배경 이미지 위에 올라가므로 투명하게 설정
        battlePanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);

        // 왼쪽 대상
        leftContestantPanel = createContestantPanel(true);
        gbc.gridx = 0;
        battlePanel.add(leftContestantPanel, gbc);

        // VS 텍스트
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 48f));
        vsLabel.setForeground(new Color(220, 20, 60));
        gbc.gridx = 1;
        gbc.insets = new Insets(20, 40, 20, 40);
        battlePanel.add(vsLabel, gbc);

        // 오른쪽 대상
        rightContestantPanel = createContestantPanel(false);
        gbc.gridx = 2;
        gbc.insets = new Insets(20, 20, 20, 20);
        battlePanel.add(rightContestantPanel, gbc);

        // TopInfoPanel과 BattlePanel을 하나의 컨테이너에 담아 CENTER에 배치
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(topInfoPanel, BorderLayout.NORTH);
        centerContainer.add(battlePanel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);
    }

    /**
     * 대상 패널을 생성합니다.
     */
    private JPanel createContestantPanel(boolean isLeft) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 450));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 이미지 레이블
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(350, 350));
        imageLabel.setBackground(new Color(240, 240, 245));
        imageLabel.setOpaque(true);
        panel.add(imageLabel, BorderLayout.CENTER);

        // 이름 레이블
        JLabel nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 20f));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.WHITE);
        panel.add(nameLabel, BorderLayout.SOUTH);

        // 호버 효과
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 240, 255));
                nameLabel.setBackground(new Color(240, 240, 255));
                panel.setBorder(BorderFactory.createLineBorder(new Color(75, 0, 130), 4));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
                nameLabel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (winnerSelectListener != null) {
                    Contestant winner = isLeft ? leftContestant : rightContestant;
                    if (winner != null) {
                        // 선택된 패널에 클릭 효과를 주기 위해 리스너 호출 전에 잠시 색상을 변경할 수도 있음
                        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 20, 60), 6));
                        winnerSelectListener.accept(winner);
                    }
                }
            }
        });

        // 레이블 참조 저장
        if (isLeft) {
            leftImageLabel = imageLabel;
            leftNameLabel = nameLabel;
        } else {
            rightImageLabel = imageLabel;
            rightNameLabel = nameLabel;
        }

        return panel;
    }

    /**
     * 대결 정보를 설정합니다.
     */
    public void setBattle(Contestant left, Contestant right, String roundName, String matchInfo) {
        this.leftContestant = left;
        this.rightContestant = right;

        // 라운드 정보 설정
        roundInfoLabel.setText(roundName);
        matchCountLabel.setText(matchInfo);

        // 왼쪽 대상 설정
        leftNameLabel.setText(left.getName());
        loadImage(leftImageLabel, left.getImagePath(), left.getName());

        // 오른쪽 대상 설정
        rightNameLabel.setText(right.getName());
        loadImage(rightImageLabel, right.getImagePath(), right.getName());

        System.out.println("🥊 대결: " + left.getName() + " VS " + right.getName());

        // 이전 매치에서 선택 효과가 남아있을 경우 초기화 (호버 효과 복구)
        resetContestantPanelStyles();
    }

    /**
     * 대결 패널의 테두리 스타일을 초기 상태로 되돌립니다.
     */
    private void resetContestantPanelStyles() {
        leftContestantPanel.setBackground(Color.WHITE);
        leftNameLabel.setBackground(Color.WHITE);
        leftContestantPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));

        rightContestantPanel.setBackground(Color.WHITE);
        rightNameLabel.setBackground(Color.WHITE);
        rightContestantPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 3));
    }


    /**
     * 이미지를 로드합니다.
     */
    private void loadImage(JLabel label, String imagePath, String name) {
        try {
            // 경로 정리 (맨 앞의 / 제거)
            String cleanPath = imagePath.startsWith("/") ? imagePath.substring(1) : imagePath;

            System.out.println("🖼️  이미지 로드 시도: " + cleanPath + " (이름: " + name + ")");

            // 리소스에서 이미지 로드 시도
            URL imageUrl = getClass().getClassLoader().getResource(cleanPath);

            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);

                if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                    // 이미지를 350x350 크기로 조정
                    Image scaledImage = icon.getImage().getScaledInstance(
                            350, 350, Image.SCALE_SMOOTH
                    );
                    label.setIcon(new ImageIcon(scaledImage));
                    label.setText("");
                    System.out.println("✅ 이미지 로드 성공: " + name);
                } else {
                    // 아이콘 크기가 0인 경우
                    setImageNotFound(label, name, "아이콘 크기 0");
                }
            } else {
                // URL이 null인 경우
                setImageNotFound(label, name, "리소스를 찾을 수 없음: " + cleanPath);
            }

        } catch (Exception e) {
            setImageNotFound(label, name, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 이미지를 찾을 수 없을 때 표시
     */
    private void setImageNotFound(JLabel label, String name, String reason) {
        label.setIcon(null);
        label.setText("<html><center>이미지 없음<br><small>" + name + "</small></center></html>");
        label.setFont(FontManager.getDungGeunMo(14f));
        label.setForeground(new Color(150, 150, 150));
        System.err.println("❌ 이미지 로드 실패: " + name + " - " + reason);
    }

    /**
     * 승자 선택 리스너를 설정합니다.
     */
    public void setWinnerSelectListener(Consumer<Contestant> listener) {
        this.winnerSelectListener = listener;
    }

    /**
     * 헤더(♥️ Pick Me) 클릭 리스너를 설정합니다.
     * @param listener 헤더 클릭 리스너
     */
    public void setBackButtonListener(Runnable listener) {
        this.backButtonListener = listener;
    }
}