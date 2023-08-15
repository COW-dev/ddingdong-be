package ddingdong.ddingdongBE.domain.fixzone.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ddingdong.ddingdongBE.common.exception.ErrorMessage;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.CreateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.controller.dto.request.UpdateFixRequest;
import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FixZoneService {

	private final FixRepository fixRepository;


	public Long create(Club club, CreateFixRequest request) {
		Fix createdFix = request.toEntity(club);
		Fix savedFix = fixRepository.save(createdFix);
		return savedFix.getId();
	}

	public void update(Long fixId, UpdateFixRequest request) {
		Fix fix = fixRepository.findById(fixId)
			.orElseThrow(() -> new IllegalArgumentException(NO_SUCH_FIX.getText()));

		fix.update(request);

	}
}
