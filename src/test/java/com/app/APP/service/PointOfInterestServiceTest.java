package com.app.APP.service;

import com.app.APP.entity.Evidence;
import com.app.APP.entity.Path;
import com.app.APP.entity.PointOfInterest;
import com.app.APP.model.dto.request.EvidenceRequest;
import com.app.APP.model.dto.request.PointOfInterestRequest;
import com.app.APP.model.dto.response.EvidenceResponse;
import com.app.APP.model.dto.response.PointOfInterestResponse;
import com.app.APP.repository.PathRepository;
import com.app.APP.repository.EvidenceRepository;
import com.app.APP.repository.PointOfInterestRepository;
import com.app.APP.stub.PathStub;
import com.app.APP.stub.PointOfInterestStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PointOfInterestService")
class PointOfInterestServiceTest {

    @Mock
    private PointOfInterestRepository pointRepository;
    @Mock
    private EvidenceRepository evidenceRepository;
    @Mock
    private PathRepository pathRepository;

    @InjectMocks
    private PointOfInterestService service;

    @Test
    @DisplayName("create should persist point when path exists")
    void shouldCreatePoint() {
        PointOfInterestRequest request = PointOfInterestStub.aRequest();
        Path path = PathStub.aPath().build();
        when(pathRepository.findById(request.pathId())).thenReturn(Optional.of(path));
        when(pointRepository.save(any(PointOfInterest.class))).thenAnswer(inv -> inv.getArgument(0));

        PointOfInterestResponse response = service.create(PointOfInterestStub.USER_ID, request);

        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.type()).isEqualTo(request.type());
        verify(pointRepository).save(any(PointOfInterest.class));
    }

    @Test
    @DisplayName("create should fail when path does not exist")
    void shouldFailCreateWithoutPath() {
        PointOfInterestRequest request = PointOfInterestStub.aRequest();
        when(pathRepository.findById(request.pathId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(PointOfInterestStub.USER_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Caminho nao encontrado");

        verify(pointRepository, never()).save(any());
    }

    @Test
    @DisplayName("addEvidence should accept capture within 50m radius")
    void shouldAcceptCloseEvidence() {
        PointOfInterest point = PointOfInterestStub.aPoint().build();
        EvidenceRequest request = PointOfInterestStub.aCloseEvidenceRequest();
        when(pointRepository.findById(request.pointId())).thenReturn(Optional.of(point));
        when(evidenceRepository.save(any(Evidence.class))).thenAnswer(inv -> inv.getArgument(0));

        EvidenceResponse response = service.addEvidence(PointOfInterestStub.USER_ID, request);

        assertThat(response.validated()).isTrue();
        assertThat(response.pointId()).isEqualTo(point.getId());
        verify(evidenceRepository).save(any(Evidence.class));
    }

    @Test
    @DisplayName("addEvidence should reject capture beyond 50m")
    void shouldRejectFarEvidence() {
        PointOfInterest point = PointOfInterestStub.aPoint().build();
        EvidenceRequest request = PointOfInterestStub.aFarEvidenceRequest();
        when(pointRepository.findById(request.pointId())).thenReturn(Optional.of(point));

        assertThatThrownBy(() -> service.addEvidence(PointOfInterestStub.USER_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("muito longe");

        verify(evidenceRepository, never()).save(any());
    }

    @Test
    @DisplayName("addEvidence should fail when point does not exist")
    void shouldFailEvidenceWithoutPoint() {
        EvidenceRequest request = PointOfInterestStub.aCloseEvidenceRequest();
        when(pointRepository.findById(request.pointId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addEvidence(PointOfInterestStub.USER_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Ponto nao encontrado");
    }

    @Test
    @DisplayName("getById should calculate maximum level (3+ users and description)")
    void shouldCalculateMaximumLevel() {
        PointOfInterest point = PointOfInterestStub.aPoint().build();
        when(pointRepository.findById(point.getId())).thenReturn(Optional.of(point));
        when(evidenceRepository.countValidatedUsersByPointId(point.getId())).thenReturn(3L);

        PointOfInterestResponse response = service.getById(point.getId());

        assertThat(response.confidenceLevel()).isEqualTo(5);
    }

    @Test
    @DisplayName("getById should calculate minimum level (no evidence, no description)")
    void shouldCalculateMinimumLevel() {
        PointOfInterest point = PointOfInterestStub.aPoint().description(null).build();
        when(pointRepository.findById(point.getId())).thenReturn(Optional.of(point));
        when(evidenceRepository.countValidatedUsersByPointId(point.getId())).thenReturn(0L);

        PointOfInterestResponse response = service.getById(point.getId());

        assertThat(response.confidenceLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("getByPath should paginate, map and calculate level via batch count")
    void shouldListByPath() {
        Pageable pageable = PageRequest.of(0, 10);
        PointOfInterest point = PointOfInterestStub.aPoint().build();
        when(pointRepository.findByPathId(PathStub.ID, pageable))
                .thenReturn(new PageImpl<>(List.of(point)));
        // batch count: [pointId, total] — 1 validated user for the point
        when(evidenceRepository.countValidatedUsersPerPoint(List.of(point.getId())))
                .thenReturn(List.<Object[]>of(new Object[]{point.getId(), 1L}));

        Page<PointOfInterestResponse> response = service.getByPath(PathStub.ID, pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).confidenceLevel()).isEqualTo(3);
    }
}
