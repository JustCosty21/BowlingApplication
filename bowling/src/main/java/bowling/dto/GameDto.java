package bowling.dto;

import com.sun.istack.NotNull;

import java.io.Serializable;

public class GameDto implements Serializable {

    private Long id;

    @NotNull
    private String gameName;

    @NotNull
    private String playersNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayersNo() {
        return playersNo;
    }

    public void setPlayersNo(String playersNo) {
        this.playersNo = playersNo;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
