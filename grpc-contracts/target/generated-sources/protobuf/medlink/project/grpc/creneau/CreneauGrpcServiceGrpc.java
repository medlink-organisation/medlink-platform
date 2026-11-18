package medlink.project.grpc.creneau;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class CreneauGrpcServiceGrpc {

  private CreneauGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.CreneauGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.creneau.GetCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getGetCreneauMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCreneau",
      requestType = medlink.project.grpc.creneau.GetCreneauRequest.class,
      responseType = medlink.project.grpc.creneau.CreneauResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.creneau.GetCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getGetCreneauMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.creneau.GetCreneauRequest, medlink.project.grpc.creneau.CreneauResponse> getGetCreneauMethod;
    if ((getGetCreneauMethod = CreneauGrpcServiceGrpc.getGetCreneauMethod) == null) {
      synchronized (CreneauGrpcServiceGrpc.class) {
        if ((getGetCreneauMethod = CreneauGrpcServiceGrpc.getGetCreneauMethod) == null) {
          CreneauGrpcServiceGrpc.getGetCreneauMethod = getGetCreneauMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.creneau.GetCreneauRequest, medlink.project.grpc.creneau.CreneauResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCreneau"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.GetCreneauRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.CreneauResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CreneauGrpcServiceMethodDescriptorSupplier("GetCreneau"))
              .build();
        }
      }
    }
    return getGetCreneauMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.creneau.CheckAvailabilityRequest,
      medlink.project.grpc.creneau.CheckAvailabilityResponse> getCheckAvailabilityMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckAvailability",
      requestType = medlink.project.grpc.creneau.CheckAvailabilityRequest.class,
      responseType = medlink.project.grpc.creneau.CheckAvailabilityResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.creneau.CheckAvailabilityRequest,
      medlink.project.grpc.creneau.CheckAvailabilityResponse> getCheckAvailabilityMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.creneau.CheckAvailabilityRequest, medlink.project.grpc.creneau.CheckAvailabilityResponse> getCheckAvailabilityMethod;
    if ((getCheckAvailabilityMethod = CreneauGrpcServiceGrpc.getCheckAvailabilityMethod) == null) {
      synchronized (CreneauGrpcServiceGrpc.class) {
        if ((getCheckAvailabilityMethod = CreneauGrpcServiceGrpc.getCheckAvailabilityMethod) == null) {
          CreneauGrpcServiceGrpc.getCheckAvailabilityMethod = getCheckAvailabilityMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.creneau.CheckAvailabilityRequest, medlink.project.grpc.creneau.CheckAvailabilityResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckAvailability"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.CheckAvailabilityRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.CheckAvailabilityResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CreneauGrpcServiceMethodDescriptorSupplier("CheckAvailability"))
              .build();
        }
      }
    }
    return getCheckAvailabilityMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReserveCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getReserveCreneauMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReserveCreneau",
      requestType = medlink.project.grpc.creneau.ReserveCreneauRequest.class,
      responseType = medlink.project.grpc.creneau.CreneauResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReserveCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getReserveCreneauMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReserveCreneauRequest, medlink.project.grpc.creneau.CreneauResponse> getReserveCreneauMethod;
    if ((getReserveCreneauMethod = CreneauGrpcServiceGrpc.getReserveCreneauMethod) == null) {
      synchronized (CreneauGrpcServiceGrpc.class) {
        if ((getReserveCreneauMethod = CreneauGrpcServiceGrpc.getReserveCreneauMethod) == null) {
          CreneauGrpcServiceGrpc.getReserveCreneauMethod = getReserveCreneauMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.creneau.ReserveCreneauRequest, medlink.project.grpc.creneau.CreneauResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReserveCreneau"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.ReserveCreneauRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.CreneauResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CreneauGrpcServiceMethodDescriptorSupplier("ReserveCreneau"))
              .build();
        }
      }
    }
    return getReserveCreneauMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReleaseCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getReleaseCreneauMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReleaseCreneau",
      requestType = medlink.project.grpc.creneau.ReleaseCreneauRequest.class,
      responseType = medlink.project.grpc.creneau.CreneauResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReleaseCreneauRequest,
      medlink.project.grpc.creneau.CreneauResponse> getReleaseCreneauMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.creneau.ReleaseCreneauRequest, medlink.project.grpc.creneau.CreneauResponse> getReleaseCreneauMethod;
    if ((getReleaseCreneauMethod = CreneauGrpcServiceGrpc.getReleaseCreneauMethod) == null) {
      synchronized (CreneauGrpcServiceGrpc.class) {
        if ((getReleaseCreneauMethod = CreneauGrpcServiceGrpc.getReleaseCreneauMethod) == null) {
          CreneauGrpcServiceGrpc.getReleaseCreneauMethod = getReleaseCreneauMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.creneau.ReleaseCreneauRequest, medlink.project.grpc.creneau.CreneauResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReleaseCreneau"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.ReleaseCreneauRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.creneau.CreneauResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CreneauGrpcServiceMethodDescriptorSupplier("ReleaseCreneau"))
              .build();
        }
      }
    }
    return getReleaseCreneauMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CreneauGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceStub>() {
        @java.lang.Override
        public CreneauGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CreneauGrpcServiceStub(channel, callOptions);
        }
      };
    return CreneauGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static CreneauGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public CreneauGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CreneauGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return CreneauGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CreneauGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceBlockingStub>() {
        @java.lang.Override
        public CreneauGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CreneauGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return CreneauGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CreneauGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CreneauGrpcServiceFutureStub>() {
        @java.lang.Override
        public CreneauGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CreneauGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return CreneauGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getCreneau(medlink.project.grpc.creneau.GetCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCreneauMethod(), responseObserver);
    }

    /**
     */
    default void checkAvailability(medlink.project.grpc.creneau.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CheckAvailabilityResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckAvailabilityMethod(), responseObserver);
    }

    /**
     */
    default void reserveCreneau(medlink.project.grpc.creneau.ReserveCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReserveCreneauMethod(), responseObserver);
    }

    /**
     */
    default void releaseCreneau(medlink.project.grpc.creneau.ReleaseCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReleaseCreneauMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CreneauGrpcService.
   */
  public static abstract class CreneauGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CreneauGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CreneauGrpcService.
   */
  public static final class CreneauGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CreneauGrpcServiceStub> {
    private CreneauGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreneauGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CreneauGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getCreneau(medlink.project.grpc.creneau.GetCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCreneauMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void checkAvailability(medlink.project.grpc.creneau.CheckAvailabilityRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CheckAvailabilityResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void reserveCreneau(medlink.project.grpc.creneau.ReserveCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReserveCreneauMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void releaseCreneau(medlink.project.grpc.creneau.ReleaseCreneauRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReleaseCreneauMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CreneauGrpcService.
   */
  public static final class CreneauGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<CreneauGrpcServiceBlockingV2Stub> {
    private CreneauGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreneauGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CreneauGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse getCreneau(medlink.project.grpc.creneau.GetCreneauRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetCreneauMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CheckAvailabilityResponse checkAvailability(medlink.project.grpc.creneau.CheckAvailabilityRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCheckAvailabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse reserveCreneau(medlink.project.grpc.creneau.ReserveCreneauRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getReserveCreneauMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse releaseCreneau(medlink.project.grpc.creneau.ReleaseCreneauRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getReleaseCreneauMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service CreneauGrpcService.
   */
  public static final class CreneauGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CreneauGrpcServiceBlockingStub> {
    private CreneauGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreneauGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CreneauGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse getCreneau(medlink.project.grpc.creneau.GetCreneauRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCreneauMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CheckAvailabilityResponse checkAvailability(medlink.project.grpc.creneau.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckAvailabilityMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse reserveCreneau(medlink.project.grpc.creneau.ReserveCreneauRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReserveCreneauMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.creneau.CreneauResponse releaseCreneau(medlink.project.grpc.creneau.ReleaseCreneauRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReleaseCreneauMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CreneauGrpcService.
   */
  public static final class CreneauGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CreneauGrpcServiceFutureStub> {
    private CreneauGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CreneauGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CreneauGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.creneau.CreneauResponse> getCreneau(
        medlink.project.grpc.creneau.GetCreneauRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCreneauMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.creneau.CheckAvailabilityResponse> checkAvailability(
        medlink.project.grpc.creneau.CheckAvailabilityRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckAvailabilityMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.creneau.CreneauResponse> reserveCreneau(
        medlink.project.grpc.creneau.ReserveCreneauRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReserveCreneauMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.creneau.CreneauResponse> releaseCreneau(
        medlink.project.grpc.creneau.ReleaseCreneauRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReleaseCreneauMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CRENEAU = 0;
  private static final int METHODID_CHECK_AVAILABILITY = 1;
  private static final int METHODID_RESERVE_CRENEAU = 2;
  private static final int METHODID_RELEASE_CRENEAU = 3;

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
        case METHODID_GET_CRENEAU:
          serviceImpl.getCreneau((medlink.project.grpc.creneau.GetCreneauRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse>) responseObserver);
          break;
        case METHODID_CHECK_AVAILABILITY:
          serviceImpl.checkAvailability((medlink.project.grpc.creneau.CheckAvailabilityRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CheckAvailabilityResponse>) responseObserver);
          break;
        case METHODID_RESERVE_CRENEAU:
          serviceImpl.reserveCreneau((medlink.project.grpc.creneau.ReserveCreneauRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse>) responseObserver);
          break;
        case METHODID_RELEASE_CRENEAU:
          serviceImpl.releaseCreneau((medlink.project.grpc.creneau.ReleaseCreneauRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.creneau.CreneauResponse>) responseObserver);
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
          getGetCreneauMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.creneau.GetCreneauRequest,
              medlink.project.grpc.creneau.CreneauResponse>(
                service, METHODID_GET_CRENEAU)))
        .addMethod(
          getCheckAvailabilityMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.creneau.CheckAvailabilityRequest,
              medlink.project.grpc.creneau.CheckAvailabilityResponse>(
                service, METHODID_CHECK_AVAILABILITY)))
        .addMethod(
          getReserveCreneauMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.creneau.ReserveCreneauRequest,
              medlink.project.grpc.creneau.CreneauResponse>(
                service, METHODID_RESERVE_CRENEAU)))
        .addMethod(
          getReleaseCreneauMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.creneau.ReleaseCreneauRequest,
              medlink.project.grpc.creneau.CreneauResponse>(
                service, METHODID_RELEASE_CRENEAU)))
        .build();
  }

  private static abstract class CreneauGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CreneauGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.project.grpc.creneau.Creneau.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CreneauGrpcService");
    }
  }

  private static final class CreneauGrpcServiceFileDescriptorSupplier
      extends CreneauGrpcServiceBaseDescriptorSupplier {
    CreneauGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class CreneauGrpcServiceMethodDescriptorSupplier
      extends CreneauGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CreneauGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (CreneauGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CreneauGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetCreneauMethod())
              .addMethod(getCheckAvailabilityMethod())
              .addMethod(getReserveCreneauMethod())
              .addMethod(getReleaseCreneauMethod())
              .build();
        }
      }
    }
    return result;
  }
}
