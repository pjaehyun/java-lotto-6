package lotto;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LottoCollection {
    public List<Lotto> lottoCollection;
    private final int numberOfLotto;

    private final Map<Integer, Integer> rankingCount = new HashMap<>();
    private final Map<Integer, Integer> rankMapping = new HashMap<>();

    public LottoCollection(int purchaseAmount) {
        this.numberOfLotto = purchaseAmount / 1000;
        this.lottoCollection = new ArrayList<>();
        setRankMapping();
        generateLottoNumbers(numberOfLotto);
    }

    private void generateLottoNumbers(int numberOfLotto) {
        for (int i = 0; i < numberOfLotto; i++) {
            Lotto lotto = new Lotto(Randoms.pickUniqueNumbersInRange(1, 45, 6));
            lottoCollection.add(lotto);
        }
    }

    public void printLottoNumbers() {
        System.out.println(numberOfLotto + "개를 구매했습니다.");
        for (Lotto lotto : lottoCollection) {
            lotto.printLottoNumbers();
        }
    }

    public void matchLottoNumbers(LottoResult result) {
        int matchingCount = 0;
        boolean matchingBonusCount = false;
        for (Lotto lotto : lottoCollection) {
            matchingCount = lotto.countMatchingLottoNumbers(result);
            matchingBonusCount = lotto.hasBonusNumber(result);
            setRank(matchingCount, matchingBonusCount);
        }
    }

    private void setRank(int matchingCount, boolean matchingBonusCount) {
        int rank = rankMapping.getOrDefault(matchingCount, 0);

        if (rank == 3 && matchingBonusCount) {
            rank = 2;
        }

        rankingCount.put(rank, rankingCount.getOrDefault(rank, 0) + 1);
    }

    private void setRankMapping() {
        rankMapping.put(3, 5);
        rankMapping.put(4, 4);
        rankMapping.put(5, 3);
        rankMapping.put(6, 1);
    }

    public void printWinningStatistics() {
        System.out.println("당첨 통계");
        System.out.println("---");
        for (int i = 5; i > 0; i--){
            LottoRank rank = getLottoRankByNumber(i);
            int count = rankingCount.getOrDefault(i, 0);
            System.out.println(rank.getRankMessage() + " - " + count + "개");
        }
    }

    public void printProfitRate() {
        double profitRate = getProfitRate();
        System.out.printf("총 수익률은 %.1f%%입니다.", profitRate);
    }

    private double getProfitRate() {
        double totalPrize = 0.0;
        for (int i = 5; i > 0; i--){
            int count = rankingCount.getOrDefault(i, 0);
            if (count == 0) {
                continue;
            }
            LottoRank rank = getLottoRankByNumber(i);
            int prize = rank.getRankPrize();
            totalPrize = totalPrize + (prize * count);
        }

        return totalPrize / (double) (numberOfLotto * 1000) * 100;
    }

    private LottoRank getLottoRankByNumber(int number) {
        for (LottoRank rank : LottoRank.values()) {
            if (rank.ordinal() + 1 == number) {
                return rank;
            }
        }
        return null;
    }
}
