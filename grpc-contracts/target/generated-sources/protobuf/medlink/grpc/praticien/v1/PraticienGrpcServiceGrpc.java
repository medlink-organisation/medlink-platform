package medlink.grpc.praticien.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class PraticienGrpcServiceGrpc {

  private PraticienGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medlink.praticien.v1.PraticienGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.CreatePraticienGrpcRequest,
      medlink.grpc.praticien.v1.PraticienGrpcResponse> getCreatePraticienMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreatePraticien",
      requestType = medlink.grpc.praticien.v1.CreatePraticienGrpcRequest.class,
      responseType = medlink.grpc.praticien.v1.PraticienGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.CreatePraticienGrpcRequest,
      medlink.grpc.praticien.v1.PraticienGrpcResponse> getCreatePraticienMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.CreatePraticienGrpcRequest, medlink.grpc.praticien.v1.PraticienGrpcResponse> getCreatePraticienMethod;
    if ((getCreatePraticienMethod = PraticienGrpcServiceGrpc.getCreatePraticienMethod) == null) {
      synchronized (PraticienGrpcServiceGrpc.class) {
        if ((getCreatePraticienMethod = PraticienGrpcServiceGrpc.getCreatePraticienMethod) == null) {
          PraticienGrpcServiceGrpc.getCreatePraticienMethod = getCreatePraticienMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.praticien.v1.CreatePraticienGrpcRequest, medlink.grpc.praticien.v1.PraticienGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreatePraticien"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.praticien.v1.CreatePraticienGrpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.praticien.v1.PraticienGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PraticienGrpcServiceMethodDescriptorSupplier("CreatePraticien"))
              .build();
        }
      }
    }
    return getCreatePraticienMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest,
      medlink.grpc.praticien.v1.PraticienGrpcResponse> getGetPraticienByKeycloakIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPraticienByKeycloakId",
      requestType = medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest.class,
      responseType = medlink.grpc.praticien.v1.PraticienGrpcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest,
      medlink.grpc.praticien.v1.PraticienGrpcResponse> getGetPraticienByKeycloakIdMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest, medlink.grpc.praticien.v1.PraticienGrpcResponse> getGetPraticienByKeycloakIdMethod;
    if ((getGetPraticienByKeycloakIdMethod = PraticienGrpcServiceGrpc.getGetPraticienByKeycloakIdMethod) == null) {
      synchronized (PraticienGrpcServiceGrpc.class) {
        if ((getGetPraticienByKeycloakIdMethod = PraticienGrpcServiceGrpc.getGetPraticienByKeycloakIdMethod) == null) {
          PraticienGrpcServiceGrpc.getGetPraticienByKeycloakIdMethod = getGetPraticienByKeycloakIdMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest, medlink.grpc.praticien.v1.PraticienGrpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPraticienByKeycloakId"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.praticien.v1.PraticienGrpcResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PraticienGrpcServiceMethodDescriptorSupplier("GetPraticienByKeycloakId"))
              .build();
        }
      }
    }
    return getGetPraticienByKeycloakIdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PraticienGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceStub>() {
        @java.lang.Override
        public PraticienGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PraticienGrpcServiceStub(channel, callOptions);
        }
      };
    return PraticienGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static PraticienGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public PraticienGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PraticienGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return PraticienGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PraticienGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceBlockingStub>() {
        @java.lang.Override
        public PraticienGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PraticienGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return PraticienGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PraticienGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PraticienGrpcServiceFutureStub>() {
        @java.lang.Override
        public PraticienGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PraticienGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return PraticienGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createPraticien(medlink.grpc.praticien.v1.CreatePraticienGrpcRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreatePraticienMethod(), responseObserver);
    }

    /**
     */
    default void getPraticienByKeycloakId(medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPraticienByKeycloakIdMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PraticienGrpcService.
   */
  public static abstract class PraticienGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PraticienGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PraticienGrpcService.
   */
  public static final class PraticienGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PraticienGrpcServiceStub> {
    private PraticienGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PraticienGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PraticienGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void createPraticien(medlink.grpc.praticien.v1.CreatePraticienGrpcRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreatePraticienMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPraticienByKeycloakId(medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPraticienByKeycloakIdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PraticienGrpcService.
   */
  public static final class PraticienGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<PraticienGrpcServiceBlockingV2Stub> {
    private PraticienGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PraticienGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PraticienGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.praticien.v1.PraticienGrpcResponse createPraticien(medlink.grpc.praticien.v1.CreatePraticienGrpcRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreatePraticienMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.praticien.v1.PraticienGrpcResponse getPraticienByKeycloakId(medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetPraticienByKeycloakIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service PraticienGrpcService.
   */
  public static final class PraticienGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PraticienGrpcServiceBlockingStub> {
    private PraticienGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PraticienGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PraticienGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.praticien.v1.PraticienGrpcResponse createPraticien(medlink.grpc.praticien.v1.CreatePraticienGrpcRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreatePraticienMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.praticien.v1.PraticienGrpcResponse getPraticienByKeycloakId(medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPraticienByKeycloakIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PraticienGrpcService.
   */
  public static final class PraticienGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PraticienGrpcServiceFutureStub> {
    private PraticienGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PraticienGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PraticienGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.praticien.v1.PraticienGrpcResponse> createPraticien(
        medlink.grpc.praticien.v1.CreatePraticienGrpcRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreatePraticienMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.praticien.v1.PraticienGrpcResponse> getPraticienByKeycloakId(
        medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPraticienByKeycloakIdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_PRATICIEN = 0;
  private static final int METHODID_GET_PRATICIEN_BY_KEYCLOAK_ID = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_PRATICIEN:
          serviceImpl.createPraticien((medlink.grpc.praticien.v1.CreatePraticienGrpcRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse>) responseObserver);
          break;
        case METHODID_GET_PRATICIEN_BY_KEYCLOAK_ID:
          serviceImpl.getPraticienByKeycloakId((medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.praticien.v1.PraticienGrpcResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreatePraticienMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.praticien.v1.CreatePraticienGrpcRequest,
              medlink.grpc.praticien.v1.PraticienGrpcResponse>(
                service, METHODID_CREATE_PRATICIEN)))
        .addMethod(
          getGetPraticienByKeycloakIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.praticien.v1.GetPraticienByKeycloakIdRequest,
              medlink.grpc.praticien.v1.PraticienGrpcResponse>(
                service, METHODID_GET_PRATICIEN_BY_KEYCLOAK_ID)))
        .build();
  }

  private static abstract class PraticienGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PraticienGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.grpc.praticien.v1.PraticienProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PraticienGrpcService");
    }
  }

  private static final class PraticienGrpcServiceFileDescriptorSupplier
      extends PraticienGrpcServiceBaseDescriptorSupplier {
    PraticienGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class PraticienGrpcServiceMethodDescriptorSupplier
      extends PraticienGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PraticienGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PraticienGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PraticienGrpcServiceFileDescriptorSupplier())
              .addMethod(getCreatePraticienMethod())
              .addMethod(getGetPraticienByKeycloakIdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
