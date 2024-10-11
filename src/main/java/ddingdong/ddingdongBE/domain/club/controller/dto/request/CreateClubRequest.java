package ddingdong.ddingdongBE.domain.club.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;

public record CreateClubRequest(
        String clubName,
        String category,
        String leaderName,
        String tag,
        String userId,
        String password
) {

    public CreateClubCommand toCommand() {
        return CreateClubCommand.builder()
                .clubName(clubName)
                .category(category)
                .tag(tag)
                .leaderName(leaderName)
                .build();
    }

}
