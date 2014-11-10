package net.rrm.ehour.domain;

public class TeamTestBuilder {
    public static TeamTestBuilder start() {
        return new TeamTestBuilder();
    }

    public Team build() {
        Team team = new Team();
        return team;
    }
}
