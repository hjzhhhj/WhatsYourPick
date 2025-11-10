package com.whatsyourpick.ui;

import com.whatsyourpick.model.Category;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

/* 카테고리 선택 화면 패널 */
public class CategoryPanel extends JPanel {

    private Consumer<Category> categorySelectListener;
    private Runnable backButtonListener;
    private JPanel gridPanel;
    private BufferedImage scaledBackground;
    private static final Color PINK_COLOR = new Color(241, 113, 151); // #F17197
    private static final Color HEADER_BG_COLOR = new Color(255, 209, 233); // #FFD1E9
    private static final Color BACKGROUND_COLOR = new Color(255, 243, 253); // #FFF3FD

    private static final int CARD_SIZE = 250;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;

    public CategoryPanel() {
        setLayout(new BorderLayout());
        loadAndScaleBackground();
        initComponents();
    }

    private void loadAndScaleBackground() {
        try {
            BufferedImage originalBg = ImageIO.read(getClass().getResourceAsStream("/images/background.png"));
            if (originalBg != null) {
                // 한 번만 리사이징해서 캐시
                scaledBackground = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = scaledBackground.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(originalBg, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
                g2d.dispose();
                // 원본 이미지는 메모리에서 해제
                originalBg.flush();
            }
        } catch (Exception e) {
            scaledBackground = null;
            System.out.println("배경 이미지를 찾을 수 없습니다.");
            if (e != null) {
                System.err.println("상세 오류: " + e.getMessage());
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (scaledBackground != null) {
            g.drawImage(scaledBackground, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void initComponents() {
        // 상단 헤더 패널 (♥️ Pick Me)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        headerPanel.setBackground(HEADER_BG_COLOR);
        headerPanel.setBorder(new MatteBorder(0, 0, 3, 0, PINK_COLOR)); // 아래쪽만 3px 테두리
        headerPanel.setOpaque(true);
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel headerLabel = new JLabel("\u2665\ufe0f  Pick Me");
        headerLabel.setFont(FontManager.getPressStart2P(Font.BOLD, 32f));
        headerLabel.setForeground(PINK_COLOR);
        headerPanel.add(headerLabel);

        // 헤더 클릭 이벤트
        headerPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonListener != null) {
                    backButtonListener.run();
                }
            }
        });

        add(headerPanel, BorderLayout.NORTH);

        // 카테고리 그리드를 담는 컨테이너 (배경 투명)
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setOpaque(false);

        // 카테고리 그리드
        gridPanel = new JPanel(new GridLayout(2, 4, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        centerContainer.add(gridPanel);
        add(centerContainer, BorderLayout.CENTER);
    }


    /**
     * 카테고리 목록을 표시합니다.
     * @param categories 카테고리 리스트
     */
    public void displayCategories(List<Category> categories) {
        gridPanel.removeAll();

        for (Category category : categories) {
            JPanel categoryCard = createCategoryCard(category);
            gridPanel.add(categoryCard);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    /**
     * 카테고리 카드를 생성합니다.
     * @param category 카테고리
     * @return 카테고리 카드 패널
     */
    private JPanel createCategoryCard(Category category) {
        RoundedPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 이미지 영역
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(240, 230));
        imageLabel.setOpaque(false);

        // 이미지 로드 시도
        try {
            BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/" + category.getImagePath()));
            if (img != null) {
                Image scaledImage = img.getScaledInstance(240, 230, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                imageLabel.setText("이미지 없음: " + category.getImagePath());
                imageLabel.setFont(FontManager.getDungGeunMo(12f));
                System.err.println("이미지를 찾을 수 없습니다: " + category.getImagePath());
            }
        } catch (Exception e) {
            imageLabel.setText("이미지 로드 실패");
            imageLabel.setFont(FontManager.getDungGeunMo(12f));
            System.err.println("이미지 로드 중 오류 발생: " + category.getImagePath());
            e.printStackTrace();
        }

        card.add(imageLabel, BorderLayout.CENTER);

        // 카테고리 이름 (개수 제거)
        JLabel nameLabel = new JLabel(category.getName(), SwingConstants.CENTER);
        nameLabel.setFont(FontManager.getDungGeunMo(Font.BOLD, 18f));
        nameLabel.setForeground(PINK_COLOR); // #F17197
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        nameLabel.setOpaque(true);
        nameLabel.setBackground(Color.WHITE);
        card.add(nameLabel, BorderLayout.SOUTH);


        // 호버 효과 및 클릭 이벤트
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(240, 240, 250));
                nameLabel.setBackground(new Color(240, 240, 250));
                card.setBorderColor(new Color(75, 0, 130));
                card.setBorderWidth(3);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                nameLabel.setBackground(Color.WHITE);
                card.setBorderColor(new Color(200, 200, 200));
                card.setBorderWidth(2);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (categorySelectListener != null) {
                    categorySelectListener.accept(category);
                }
            }
        });

        return card;
    }

    /**
     * 둥근 모서리를 가진 패널 클래스
     */
    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color borderColor = new Color(200, 200, 200);
        private int borderWidth = 2;

        public RoundedPanel(int cornerRadius) {
            super();
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        public void setBorderColor(Color color) {
            this.borderColor = color;
            repaint();
        }

        public void setBorderWidth(int width) {
            this.borderWidth = width;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 배경 그리기
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));

            // 테두리 그리기
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(borderWidth));
            g2.draw(new RoundRectangle2D.Double(borderWidth / 2.0, borderWidth / 2.0,
                    getWidth() - borderWidth, getHeight() - borderWidth, cornerRadius, cornerRadius));

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * 카테고리 선택 리스너를 설정합니다.
     * @param listener 카테고리 선택 리스너
     */
    public void setCategorySelectListener(Consumer<Category> listener) {
        this.categorySelectListener = listener;
    }

    /**
     * 헤더(♥️ Pick Me) 클릭 리스너를 설정합니다.
     * @param listener 헤더 클릭 리스너
     */
    public void setBackButtonListener(Runnable listener) {
        this.backButtonListener = listener;
    }
}