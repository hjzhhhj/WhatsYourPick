package com.whatsyourpick.game;

import com.whatsyourpick.model.Contestant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 토너먼트 게임 로직을 관리하는 클래스
 */
public class TournamentManager {

    private List<Contestant> allContestants;      // 전체 후보자
    private List<Contestant> currentRound;        // 현재 라운드 후보자
    private List<Contestant> nextRound;           // 다음 라운드로 진출한 후보자
    private int tournamentSize;                    // 토너먼트 규모 (4, 8, 16, 32, 64)
    private int currentMatchIndex;                 // 현재 매치 인덱스
    private int totalMatches;                      // 현재 라운드 총 매치 수

    public TournamentManager() {
        this.allContestants = new ArrayList<>();
        this.currentRound = new ArrayList<>();
        this.nextRound = new ArrayList<>();
        this.currentMatchIndex = 0;
    }

    /**
     * 토너먼트를 초기화합니다.
     * @param contestants 전체 후보자 리스트
     * @param size 토너먼트 규모 (4, 8, 16, 32, 64)
     */
    public void initializeTournament(List<Contestant> contestants, int size) {
        this.allContestants = new ArrayList<>(contestants);
        this.tournamentSize = size;
        this.currentMatchIndex = 0;

        // 무작위로 섞기
        Collections.shuffle(allContestants);

        // 토너먼트 크기만큼만 선택
        currentRound = new ArrayList<>(allContestants.subList(0, Math.min(size, allContestants.size())));
        nextRound = new ArrayList<>();
        totalMatches = currentRound.size() / 2;
    }

    /**
     * 현재 매치의 왼쪽 후보자를 반환합니다.
     * @return 왼쪽 후보자
     */
    public Contestant getLeftContestant() {
        if (currentMatchIndex * 2 < currentRound.size()) {
            return currentRound.get(currentMatchIndex * 2);
        }
        return null;
    }

    /**
     * 현재 매치의 오른쪽 후보자를 반환합니다.
     * @return 오른쪽 후보자
     */
    public Contestant getRightContestant() {
        if (currentMatchIndex * 2 + 1 < currentRound.size()) {
            return currentRound.get(currentMatchIndex * 2 + 1);
        }
        return null;
    }

    /**
     * 승자를 선택하고 다음 매치로 진행합니다.
     * @param winner 승리한 후보자
     * @return 토너먼트가 계속 진행되면 true, 우승자가 결정되면 false
     */
    public boolean selectWinner(Contestant winner) {
        nextRound.add(winner);
        currentMatchIndex++;

        // 현재 라운드의 모든 매치가 끝났는지 확인
        if (currentMatchIndex >= totalMatches) {
            // 우승자가 결정되었는지 확인
            if (nextRound.size() == 1) {
                return false; // 토너먼트 종료
            }

            // 다음 라운드로 진행
            currentRound = new ArrayList<>(nextRound);
            nextRound = new ArrayList<>();
            currentMatchIndex = 0;
            totalMatches = currentRound.size() / 2;
        }

        return true; // 토너먼트 계속 진행
    }

    /**
     * 우승자를 반환합니다.
     * @return 우승자
     */
    public Contestant getWinner() {
        if (nextRound.size() == 1) {
            return nextRound.get(0);
        }
        return null;
    }

    /**
     * 현재 라운드 이름을 반환합니다. (결승, 4강, 8강 등)
     * @return 라운드 이름
     */
    public String getCurrentRoundName() {
        int remainingContestants = currentRound.size();
        if (remainingContestants == 2) {
            return "결승";
        } else if (remainingContestants == 4) {
            return "4강";
        } else if (remainingContestants == 8) {
            return "8강";
        } else if (remainingContestants == 16) {
            return "16강";
        } else if (remainingContestants == 32) {
            return "32강";
        } else if (remainingContestants == 64) {
            return "64강";
        }
        return remainingContestants + "강";
    }

    /**
     * 현재 매치 정보를 반환합니다. (예: "1/4")
     * @return 매치 정보 문자열
     */
    public String getCurrentMatchInfo() {
        return (currentMatchIndex + 1) + "/" + totalMatches;
    }

    /**
     * 토너먼트가 시작되었는지 확인합니다.
     * @return 시작 여부
     */
    public boolean isStarted() {
        return !currentRound.isEmpty();
    }

    /**
     * 토너먼트를 초기화합니다.
     */
    public void reset() {
        allContestants.clear();
        currentRound.clear();
        nextRound.clear();
        currentMatchIndex = 0;
        tournamentSize = 0;
        totalMatches = 0;
    }
}