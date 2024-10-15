package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.service.dto.CreateFileMetaDataCommand;

public interface FacadeFileMetaDataService {

    void create(CreateFileMetaDataCommand command);

}
