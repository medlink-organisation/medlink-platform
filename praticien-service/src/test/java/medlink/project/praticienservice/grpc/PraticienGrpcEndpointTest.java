package medlink.project.praticienservice.grpc;

import io.grpc.stub.StreamObserver;
import medlink.grpc.praticien.v1.GetPraticiensByIdsRequest;
import medlink.grpc.praticien.v1.PraticienGrpcResponse;
import medlink.grpc.praticien.v1.PraticiensByIdsResponse;
import medlink.project.praticienservice.dto.PraticienDto;
import medlink.project.praticienservice.service.PraticienService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PraticienGrpcEndpointTest {

    @Mock
    private PraticienService praticienService;

    @Mock
    private PraticienGrpcMapper grpcMapper;

    @Mock
    private StreamObserver<PraticiensByIdsResponse> responseObserver;

    @InjectMocks
    private PraticienGrpcEndpoint endpoint;

    @Test
    void returnsRequestedPraticiensInOneBatch() {
        PraticienDto first = PraticienDto.builder().id(12L).nom("Trabelsi").build();
        PraticienDto second = PraticienDto.builder().id(13L).nom("Mansour").build();
        PraticienGrpcResponse firstResponse = PraticienGrpcResponse.newBuilder()
                .setId(12L)
                .setNom("Trabelsi")
                .build();
        PraticienGrpcResponse secondResponse = PraticienGrpcResponse.newBuilder()
                .setId(13L)
                .setNom("Mansour")
                .build();

        when(praticienService.getByIds(List.of(12L, 13L)))
                .thenReturn(List.of(first, second));
        when(grpcMapper.toGrpc(first)).thenReturn(firstResponse);
        when(grpcMapper.toGrpc(second)).thenReturn(secondResponse);

        endpoint.getPraticiensByIds(
                GetPraticiensByIdsRequest.newBuilder()
                        .addAllPraticienIds(List.of(12L, 13L))
                        .build(),
                responseObserver
        );

        ArgumentCaptor<PraticiensByIdsResponse> responseCaptor =
                ArgumentCaptor.forClass(PraticiensByIdsResponse.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        verify(responseObserver).onCompleted();
        assertThat(responseCaptor.getValue().getPraticiensList())
                .extracting(PraticienGrpcResponse::getId)
                .containsExactly(12L, 13L);
    }
}
