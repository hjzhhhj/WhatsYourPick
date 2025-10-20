package com.whatsyourpick;

import com.whatsyourpick.database.DatabaseManager;
import com.whatsyourpick.database.JdbcManager;
import com.whatsyourpick.game.TournamentManager;
import com.whatsyourpick.model.Category;
import com.whatsyourpick.model.Contestant;
import com.whatsyourpick.ui.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 이상형 월드컵 메인 애플리케이션 클래스
 */
public class Main extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private DatabaseManager databaseManager;
    private TournamentManager tournamentManager;

    private StartPanel startPanel;
    private CategoryPanel categoryPanel;
    private TournamentSetupPanel tournamentSetupPanel;
    private BattlePanel battlePanel;
    private ResultPanel resultPanel;

    private Category selectedCategory;

    public Main() {
        // 폰트 초기화
        FontManager.initialize();

        // 데이터베이스 매니저 초기화 (나중에 실제 DB로 교체)
        databaseManager = new JdbcManager();
        databaseManager.initialize();

        // 토너먼트 매니저 초기화
        tournamentManager = new TournamentManager();

        // UI 초기화
        initializeUI();

        // 윈도우 설정
        setTitle("What's Your Pick? - Ideal Type World Cup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        // 프로그램 종료 시 DB 연결 해제
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                databaseManager.close();
            }
        });
    }

    private void initializeUI() {
        // CardLayout으로 화면 전환 관리
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면 패널 생성
        startPanel = new StartPanel();
        categoryPanel = new CategoryPanel();
        tournamentSetupPanel = new TournamentSetupPanel();
        battlePanel = new BattlePanel();
        resultPanel = new ResultPanel();

        // 패널을 CardLayout에 추가
        mainPanel.add(startPanel, "START");
        mainPanel.add(categoryPanel, "CATEGORY");
        mainPanel.add(tournamentSetupPanel, "SETUP");
        mainPanel.add(battlePanel, "BATTLE");
        mainPanel.add(resultPanel, "RESULT");

        // 이벤트 리스너 설정
        setupEventListeners();

        add(mainPanel);
    }

    private void setupEventListeners() {
        // 시작 화면 -> 카테고리 선택 화면
        startPanel.addStartButtonListener(e -> showCategoryScreen());

        // 카테고리 선택 리스너
        categoryPanel.setCategorySelectListener(category -> {
            selectedCategory = category;
            tournamentSetupPanel.setSelectedCategory(category);
            cardLayout.show(mainPanel, "SETUP");
        });

        // 토너먼트 시작 리스너
        tournamentSetupPanel.addStartButtonListener(e -> startTournament());

        // 대결 승자 선택 리스너
        battlePanel.setWinnerSelectListener(winner -> {
            boolean continueGame = tournamentManager.selectWinner(winner);

            if (continueGame) {
                // 다음 매치 진행
                showNextBattle();
            } else {
                // 토너먼트 종료, 결과 화면 표시
                showResult();
            }
        });

        // 결과 화면 버튼 리스너
        resultPanel.addAnotherGamesListener(e -> showCategoryScreen());
        resultPanel.addRestartListener(e -> restartTournament());
    }

    /**
     * 카테고리 선택 화면을 표시합니다.
     */
    private void showCategoryScreen() {
        List<Category> categories = databaseManager.getAllCategories();
        categoryPanel.displayCategories(categories);
        cardLayout.show(mainPanel, "CATEGORY");
    }

    /**
     * 토너먼트를 시작합니다.
     */
    private void startTournament() {
        int round = tournamentSetupPanel.getSelectedRound();
        Category category = tournamentSetupPanel.getSelectedCategory();

        // 해당 카테고리의 후보자들을 가져옴
        List<Contestant> contestants = databaseManager.getContestantsByCategory(category.getName());

        // 토너먼트 초기화
        tournamentManager.initializeTournament(contestants, round);

        // 첫 번째 대결 화면 표시
        showNextBattle();
    }

    /**
     * 다음 대결 화면을 표시합니다.
     */
    private void showNextBattle() {
        Contestant left = tournamentManager.getLeftContestant();
        Contestant right = tournamentManager.getRightContestant();
        String roundName = tournamentManager.getCurrentRoundName();
        String matchInfo = tournamentManager.getCurrentMatchInfo();

        battlePanel.setBattle(left, right, roundName, matchInfo);
        cardLayout.show(mainPanel, "BATTLE");
    }

    /**
     * 결과 화면을 표시합니다.
     */
    private void showResult() {
        Contestant winner = tournamentManager.getWinner();
        resultPanel.setWinner(winner, selectedCategory.getName());
        cardLayout.show(mainPanel, "RESULT");
    }

    /**
     * 현재 카테고리로 토너먼트를 재시작합니다.
     */
    private void restartTournament() {
        tournamentSetupPanel.reset();
        tournamentSetupPanel.setSelectedCategory(selectedCategory);
        cardLayout.show(mainPanel, "SETUP");
    }

    public static void main(String[] args) {
        // Look and Feel 설정
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // EDT(Event Dispatch Thread)에서 GUI 실행
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}