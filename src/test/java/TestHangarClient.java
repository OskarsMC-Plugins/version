import com.oskarsmc.version.model.HangarPluginVersion;
import com.oskarsmc.version.web.HangarClient;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class TestHangarClient {
    @Test
    public void testLatestVersion() {
        HangarClient client = HangarClient.of(HangarClient.HANGAR_PAPER_DEV);
        HangarPluginVersion latestVersion = client.getLatestVersion("oskarzyg", "test", "ForCiTesting", "velocity");
        assert latestVersion != null;
        assert Objects.equals(latestVersion.name(), "1.2.0");
    }
}
