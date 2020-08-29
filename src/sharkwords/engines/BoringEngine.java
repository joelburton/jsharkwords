package sharkwords.engines;

@Registration.RegistersEngine(name = "boring")
public class BoringEngine extends NormalEngine {
    @Override
    public String chooseAnswer() {
        return "apple";
    }
}
