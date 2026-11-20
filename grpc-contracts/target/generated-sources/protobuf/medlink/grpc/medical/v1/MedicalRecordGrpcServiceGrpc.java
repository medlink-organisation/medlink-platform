package medlink.grpc.medical.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class MedicalRecordGrpcServiceGrpc {

  private MedicalRecordGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.MedicalRecordGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.grpc.medical.v1.GetRecordMetadataRequest,
      medlink.grpc.medical.v1.RecordMetadataResponse> getGetRecordMetadataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRecordMetadata",
      requestType = medlink.grpc.medical.v1.GetRecordMetadataRequest.class,
      responseType = medlink.grpc.medical.v1.RecordMetadataResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.medical.v1.GetRecordMetadataRequest,
      medlink.grpc.medical.v1.RecordMetadataResponse> getGetRecordMetadataMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.medical.v1.GetRecordMetadataRequest, medlink.grpc.medical.v1.RecordMetadataResponse> getGetRecordMetadataMethod;
    if ((getGetRecordMetadataMethod = MedicalRecordGrpcServiceGrpc.getGetRecordMetadataMethod) == null) {
      synchronized (MedicalRecordGrpcServiceGrpc.class) {
        if ((getGetRecordMetadataMethod = MedicalRecordGrpcServiceGrpc.getGetRecordMetadataMethod) == null) {
          MedicalRecordGrpcServiceGrpc.getGetRecordMetadataMethod = getGetRecordMetadataMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.medical.v1.GetRecordMetadataRequest, medlink.grpc.medical.v1.RecordMetadataResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRecordMetadata"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.medical.v1.GetRecordMetadataRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.medical.v1.RecordMetadataResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalRecordGrpcServiceMethodDescriptorSupplier("GetRecordMetadata"))
              .build();
        }
      }
    }
    return getGetRecordMetadataMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.medical.v1.AttachValidatedExamRequest,
      medlink.grpc.medical.v1.AttachValidatedExamResponse> getAttachValidatedExamMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AttachValidatedExam",
      requestType = medlink.grpc.medical.v1.AttachValidatedExamRequest.class,
      responseType = medlink.grpc.medical.v1.AttachValidatedExamResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.medical.v1.AttachValidatedExamRequest,
      medlink.grpc.medical.v1.AttachValidatedExamResponse> getAttachValidatedExamMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.medical.v1.AttachValidatedExamRequest, medlink.grpc.medical.v1.AttachValidatedExamResponse> getAttachValidatedExamMethod;
    if ((getAttachValidatedExamMethod = MedicalRecordGrpcServiceGrpc.getAttachValidatedExamMethod) == null) {
      synchronized (MedicalRecordGrpcServiceGrpc.class) {
        if ((getAttachValidatedExamMethod = MedicalRecordGrpcServiceGrpc.getAttachValidatedExamMethod) == null) {
          MedicalRecordGrpcServiceGrpc.getAttachValidatedExamMethod = getAttachValidatedExamMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.medical.v1.AttachValidatedExamRequest, medlink.grpc.medical.v1.AttachValidatedExamResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AttachValidatedExam"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.medical.v1.AttachValidatedExamRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.medical.v1.AttachValidatedExamResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MedicalRecordGrpcServiceMethodDescriptorSupplier("AttachValidatedExam"))
              .build();
        }
      }
    }
    return getAttachValidatedExamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MedicalRecordGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceStub>() {
        @java.lang.Override
        public MedicalRecordGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalRecordGrpcServiceStub(channel, callOptions);
        }
      };
    return MedicalRecordGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static MedicalRecordGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public MedicalRecordGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalRecordGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return MedicalRecordGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MedicalRecordGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceBlockingStub>() {
        @java.lang.Override
        public MedicalRecordGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalRecordGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return MedicalRecordGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MedicalRecordGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MedicalRecordGrpcServiceFutureStub>() {
        @java.lang.Override
        public MedicalRecordGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MedicalRecordGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return MedicalRecordGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getRecordMetadata(medlink.grpc.medical.v1.GetRecordMetadataRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.RecordMetadataResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRecordMetadataMethod(), responseObserver);
    }

    /**
     */
    default void attachValidatedExam(medlink.grpc.medical.v1.AttachValidatedExamRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.AttachValidatedExamResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAttachValidatedExamMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MedicalRecordGrpcService.
   */
  public static abstract class MedicalRecordGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MedicalRecordGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MedicalRecordGrpcService.
   */
  public static final class MedicalRecordGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MedicalRecordGrpcServiceStub> {
    private MedicalRecordGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalRecordGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalRecordGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getRecordMetadata(medlink.grpc.medical.v1.GetRecordMetadataRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.RecordMetadataResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRecordMetadataMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void attachValidatedExam(medlink.grpc.medical.v1.AttachValidatedExamRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.AttachValidatedExamResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAttachValidatedExamMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MedicalRecordGrpcService.
   */
  public static final class MedicalRecordGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<MedicalRecordGrpcServiceBlockingV2Stub> {
    private MedicalRecordGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalRecordGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalRecordGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.medical.v1.RecordMetadataResponse getRecordMetadata(medlink.grpc.medical.v1.GetRecordMetadataRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetRecordMetadataMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.medical.v1.AttachValidatedExamResponse attachValidatedExam(medlink.grpc.medical.v1.AttachValidatedExamRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getAttachValidatedExamMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service MedicalRecordGrpcService.
   */
  public static final class MedicalRecordGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MedicalRecordGrpcServiceBlockingStub> {
    private MedicalRecordGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalRecordGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalRecordGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.medical.v1.RecordMetadataResponse getRecordMetadata(medlink.grpc.medical.v1.GetRecordMetadataRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRecordMetadataMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.medical.v1.AttachValidatedExamResponse attachValidatedExam(medlink.grpc.medical.v1.AttachValidatedExamRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAttachValidatedExamMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MedicalRecordGrpcService.
   */
  public static final class MedicalRecordGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MedicalRecordGrpcServiceFutureStub> {
    private MedicalRecordGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MedicalRecordGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MedicalRecordGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.medical.v1.RecordMetadataResponse> getRecordMetadata(
        medlink.grpc.medical.v1.GetRecordMetadataRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRecordMetadataMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.medical.v1.AttachValidatedExamResponse> attachValidatedExam(
        medlink.grpc.medical.v1.AttachValidatedExamRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAttachValidatedExamMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_RECORD_METADATA = 0;
  private static final int METHODID_ATTACH_VALIDATED_EXAM = 1;

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
        case METHODID_GET_RECORD_METADATA:
          serviceImpl.getRecordMetadata((medlink.grpc.medical.v1.GetRecordMetadataRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.RecordMetadataResponse>) responseObserver);
          break;
        case METHODID_ATTACH_VALIDATED_EXAM:
          serviceImpl.attachValidatedExam((medlink.grpc.medical.v1.AttachValidatedExamRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.medical.v1.AttachValidatedExamResponse>) responseObserver);
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
          getGetRecordMetadataMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.medical.v1.GetRecordMetadataRequest,
              medlink.grpc.medical.v1.RecordMetadataResponse>(
                service, METHODID_GET_RECORD_METADATA)))
        .addMethod(
          getAttachValidatedExamMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.medical.v1.AttachValidatedExamRequest,
              medlink.grpc.medical.v1.AttachValidatedExamResponse>(
                service, METHODID_ATTACH_VALIDATED_EXAM)))
        .build();
  }

  private static abstract class MedicalRecordGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MedicalRecordGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.grpc.medical.v1.MedicalRecord.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MedicalRecordGrpcService");
    }
  }

  private static final class MedicalRecordGrpcServiceFileDescriptorSupplier
      extends MedicalRecordGrpcServiceBaseDescriptorSupplier {
    MedicalRecordGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class MedicalRecordGrpcServiceMethodDescriptorSupplier
      extends MedicalRecordGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MedicalRecordGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MedicalRecordGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MedicalRecordGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetRecordMetadataMethod())
              .addMethod(getAttachValidatedExamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
