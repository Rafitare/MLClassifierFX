package evaluation;

import java.util.ArrayList;
import java.util.List;

public class ConfusionMatrix {
    private int TP;
    private int FP;
    private int TN;
    private int FN;

    public ConfusionMatrix() {
        this.TP = 0;
        this.FP = 0;
        this.TN = 0;
        this.FN = 0;
    }

    public void update(List<Integer> actual, List<Integer> predicted) {
        for (int i = 0; i < actual.size(); i++) {
            if (actual.get(i) == 1 && predicted.get(i) == 1) {
                TP++;
            } else if (actual.get(i) == 0 && predicted.get(i) == 1) {
                FP++;
            } else if (actual.get(i) == 0 && predicted.get(i) == 0) {
                TN++;
            } else if (actual.get(i) == 1 && predicted.get(i) == 0) {
                FN++;
            }
        }
    }

    public List<String[]> getMatrixAsList() {
        List<String[]> matrix = new ArrayList<>();
        matrix.add(new String[]{"Actual / Predicted", "Predicted 0", "Predicted 1"});
        matrix.add(new String[]{"Actual 0", String.valueOf(TN), String.valueOf(FP)});
        matrix.add(new String[]{"Actual 1", String.valueOf(FN), String.valueOf(TP)});
        return matrix;
    }

    @Override
    public String toString() {
        return "Confusion Matrix:\n" +
                "TP: " + TP + "\n" +
                "FP: " + FP + "\n" +
                "TN: " + TN + "\n" +
                "FN: " + FN + "\n";
    }
}