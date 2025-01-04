package modelDomain;
import java.util.List;

public interface Model<F, L>{
    void train(List<Instance<F, L>> instances );
    List<L> test(List<Instance<F, L>> instances);
}
