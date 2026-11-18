package medlink.project.grpc.directory;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class DirectoryGrpcServiceGrpc {

  private DirectoryGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.DirectoryGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetClinicByIdRequest,
      medlink.project.grpc.directory.ClinicResponse> getGetClinicByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetClinicById",
      requestType = medlink.project.grpc.directory.GetClinicByIdRequest.class,
      responseType = medlink.project.grpc.directory.ClinicResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetClinicByIdRequest,
      medlink.project.grpc.directory.ClinicResponse> getGetClinicByIdMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetClinicByIdRequest, medlink.project.grpc.directory.ClinicResponse> getGetClinicByIdMethod;
    if ((getGetClinicByIdMethod = DirectoryGrpcServiceGrpc.getGetClinicByIdMethod) == null) {
      synchronized (DirectoryGrpcServiceGrpc.class) {
        if ((getGetClinicByIdMethod = DirectoryGrpcServiceGrpc.getGetClinicByIdMethod) == null) {
          DirectoryGrpcServiceGrpc.getGetClinicByIdMethod = getGetClinicByIdMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.directory.GetClinicByIdRequest, medlink.project.grpc.directory.ClinicResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetClinicById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.directory.GetClinicByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.directory.ClinicResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DirectoryGrpcServiceMethodDescriptorSupplier("GetClinicById"))
              .build();
        }
      }
    }
    return getGetClinicByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetSpecialiteByIdRequest,
      medlink.project.grpc.directory.SpecialiteResponse> getGetSpecialiteByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSpecialiteById",
      requestType = medlink.project.grpc.directory.GetSpecialiteByIdRequest.class,
      responseType = medlink.project.grpc.directory.SpecialiteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetSpecialiteByIdRequest,
      medlink.project.grpc.directory.SpecialiteResponse> getGetSpecialiteByIdMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.directory.GetSpecialiteByIdRequest, medlink.project.grpc.directory.SpecialiteResponse> getGetSpecialiteByIdMethod;
    if ((getGetSpecialiteByIdMethod = DirectoryGrpcServiceGrpc.getGetSpecialiteByIdMethod) == null) {
      synchronized (DirectoryGrpcServiceGrpc.class) {
        if ((getGetSpecialiteByIdMethod = DirectoryGrpcServiceGrpc.getGetSpecialiteByIdMethod) == null) {
          DirectoryGrpcServiceGrpc.getGetSpecialiteByIdMethod = getGetSpecialiteByIdMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.directory.GetSpecialiteByIdRequest, medlink.project.grpc.directory.SpecialiteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSpecialiteById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.directory.GetSpecialiteByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.directory.SpecialiteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new DirectoryGrpcServiceMethodDescriptorSupplier("GetSpecialiteById"))
              .build();
        }
      }
    }
    return getGetSpecialiteByIdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DirectoryGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceStub>() {
        @java.lang.Override
        public DirectoryGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DirectoryGrpcServiceStub(channel, callOptions);
        }
      };
    return DirectoryGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static DirectoryGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public DirectoryGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DirectoryGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return DirectoryGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DirectoryGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceBlockingStub>() {
        @java.lang.Override
        public DirectoryGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DirectoryGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return DirectoryGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DirectoryGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DirectoryGrpcServiceFutureStub>() {
        @java.lang.Override
        public DirectoryGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DirectoryGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return DirectoryGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getClinicById(medlink.project.grpc.directory.GetClinicByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.directory.ClinicResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetClinicByIdMethod(), responseObserver);
    }

    /**
     */
    default void getSpecialiteById(medlink.project.grpc.directory.GetSpecialiteByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.directory.SpecialiteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSpecialiteByIdMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DirectoryGrpcService.
   */
  public static abstract class DirectoryGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DirectoryGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DirectoryGrpcService.
   */
  public static final class DirectoryGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<DirectoryGrpcServiceStub> {
    private DirectoryGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DirectoryGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DirectoryGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getClinicById(medlink.project.grpc.directory.GetClinicByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.directory.ClinicResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetClinicByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getSpecialiteById(medlink.project.grpc.directory.GetSpecialiteByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.directory.SpecialiteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSpecialiteByIdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DirectoryGrpcService.
   */
  public static final class DirectoryGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<DirectoryGrpcServiceBlockingV2Stub> {
    private DirectoryGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DirectoryGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DirectoryGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.directory.ClinicResponse getClinicById(medlink.project.grpc.directory.GetClinicByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetClinicByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.directory.SpecialiteResponse getSpecialiteById(medlink.project.grpc.directory.GetSpecialiteByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetSpecialiteByIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service DirectoryGrpcService.
   */
  public static final class DirectoryGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DirectoryGrpcServiceBlockingStub> {
    private DirectoryGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DirectoryGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DirectoryGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.directory.ClinicResponse getClinicById(medlink.project.grpc.directory.GetClinicByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetClinicByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.directory.SpecialiteResponse getSpecialiteById(medlink.project.grpc.directory.GetSpecialiteByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSpecialiteByIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DirectoryGrpcService.
   */
  public static final class DirectoryGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<DirectoryGrpcServiceFutureStub> {
    private DirectoryGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DirectoryGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DirectoryGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.directory.ClinicResponse> getClinicById(
        medlink.project.grpc.directory.GetClinicByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetClinicByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.directory.SpecialiteResponse> getSpecialiteById(
        medlink.project.grpc.directory.GetSpecialiteByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSpecialiteByIdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CLINIC_BY_ID = 0;
  private static final int METHODID_GET_SPECIALITE_BY_ID = 1;

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
        case METHODID_GET_CLINIC_BY_ID:
          serviceImpl.getClinicById((medlink.project.grpc.directory.GetClinicByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.directory.ClinicResponse>) responseObserver);
          break;
        case METHODID_GET_SPECIALITE_BY_ID:
          serviceImpl.getSpecialiteById((medlink.project.grpc.directory.GetSpecialiteByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.directory.SpecialiteResponse>) responseObserver);
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
          getGetClinicByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.directory.GetClinicByIdRequest,
              medlink.project.grpc.directory.ClinicResponse>(
                service, METHODID_GET_CLINIC_BY_ID)))
        .addMethod(
          getGetSpecialiteByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.directory.GetSpecialiteByIdRequest,
              medlink.project.grpc.directory.SpecialiteResponse>(
                service, METHODID_GET_SPECIALITE_BY_ID)))
        .build();
  }

  private static abstract class DirectoryGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DirectoryGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.project.grpc.directory.Directory.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DirectoryGrpcService");
    }
  }

  private static final class DirectoryGrpcServiceFileDescriptorSupplier
      extends DirectoryGrpcServiceBaseDescriptorSupplier {
    DirectoryGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class DirectoryGrpcServiceMethodDescriptorSupplier
      extends DirectoryGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DirectoryGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DirectoryGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DirectoryGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetClinicByIdMethod())
              .addMethod(getGetSpecialiteByIdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
