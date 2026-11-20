package medlink.grpc.workflow.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class WorkflowGrpcServiceGrpc {

  private WorkflowGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.WorkflowGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest,
      medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> getStartAppointmentWorkflowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StartAppointmentWorkflow",
      requestType = medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest.class,
      responseType = medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest,
      medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> getStartAppointmentWorkflowMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest, medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> getStartAppointmentWorkflowMethod;
    if ((getStartAppointmentWorkflowMethod = WorkflowGrpcServiceGrpc.getStartAppointmentWorkflowMethod) == null) {
      synchronized (WorkflowGrpcServiceGrpc.class) {
        if ((getStartAppointmentWorkflowMethod = WorkflowGrpcServiceGrpc.getStartAppointmentWorkflowMethod) == null) {
          WorkflowGrpcServiceGrpc.getStartAppointmentWorkflowMethod = getStartAppointmentWorkflowMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest, medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StartAppointmentWorkflow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkflowGrpcServiceMethodDescriptorSupplier("StartAppointmentWorkflow"))
              .build();
        }
      }
    }
    return getStartAppointmentWorkflowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.GetWorkflowStatusRequest,
      medlink.grpc.workflow.v1.GetWorkflowStatusResponse> getGetWorkflowStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetWorkflowStatus",
      requestType = medlink.grpc.workflow.v1.GetWorkflowStatusRequest.class,
      responseType = medlink.grpc.workflow.v1.GetWorkflowStatusResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.GetWorkflowStatusRequest,
      medlink.grpc.workflow.v1.GetWorkflowStatusResponse> getGetWorkflowStatusMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.GetWorkflowStatusRequest, medlink.grpc.workflow.v1.GetWorkflowStatusResponse> getGetWorkflowStatusMethod;
    if ((getGetWorkflowStatusMethod = WorkflowGrpcServiceGrpc.getGetWorkflowStatusMethod) == null) {
      synchronized (WorkflowGrpcServiceGrpc.class) {
        if ((getGetWorkflowStatusMethod = WorkflowGrpcServiceGrpc.getGetWorkflowStatusMethod) == null) {
          WorkflowGrpcServiceGrpc.getGetWorkflowStatusMethod = getGetWorkflowStatusMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.workflow.v1.GetWorkflowStatusRequest, medlink.grpc.workflow.v1.GetWorkflowStatusResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetWorkflowStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.GetWorkflowStatusRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.GetWorkflowStatusResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkflowGrpcServiceMethodDescriptorSupplier("GetWorkflowStatus"))
              .build();
        }
      }
    }
    return getGetWorkflowStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest,
      medlink.grpc.workflow.v1.WorkflowEventResponse> getCancelAppointmentWorkflowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelAppointmentWorkflow",
      requestType = medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest.class,
      responseType = medlink.grpc.workflow.v1.WorkflowEventResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest,
      medlink.grpc.workflow.v1.WorkflowEventResponse> getCancelAppointmentWorkflowMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest, medlink.grpc.workflow.v1.WorkflowEventResponse> getCancelAppointmentWorkflowMethod;
    if ((getCancelAppointmentWorkflowMethod = WorkflowGrpcServiceGrpc.getCancelAppointmentWorkflowMethod) == null) {
      synchronized (WorkflowGrpcServiceGrpc.class) {
        if ((getCancelAppointmentWorkflowMethod = WorkflowGrpcServiceGrpc.getCancelAppointmentWorkflowMethod) == null) {
          WorkflowGrpcServiceGrpc.getCancelAppointmentWorkflowMethod = getCancelAppointmentWorkflowMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest, medlink.grpc.workflow.v1.WorkflowEventResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelAppointmentWorkflow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.WorkflowEventResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkflowGrpcServiceMethodDescriptorSupplier("CancelAppointmentWorkflow"))
              .build();
        }
      }
    }
    return getCancelAppointmentWorkflowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.RequestRescheduleRequest,
      medlink.grpc.workflow.v1.WorkflowEventResponse> getRequestRescheduleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RequestReschedule",
      requestType = medlink.grpc.workflow.v1.RequestRescheduleRequest.class,
      responseType = medlink.grpc.workflow.v1.WorkflowEventResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.RequestRescheduleRequest,
      medlink.grpc.workflow.v1.WorkflowEventResponse> getRequestRescheduleMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.workflow.v1.RequestRescheduleRequest, medlink.grpc.workflow.v1.WorkflowEventResponse> getRequestRescheduleMethod;
    if ((getRequestRescheduleMethod = WorkflowGrpcServiceGrpc.getRequestRescheduleMethod) == null) {
      synchronized (WorkflowGrpcServiceGrpc.class) {
        if ((getRequestRescheduleMethod = WorkflowGrpcServiceGrpc.getRequestRescheduleMethod) == null) {
          WorkflowGrpcServiceGrpc.getRequestRescheduleMethod = getRequestRescheduleMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.workflow.v1.RequestRescheduleRequest, medlink.grpc.workflow.v1.WorkflowEventResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RequestReschedule"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.RequestRescheduleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.workflow.v1.WorkflowEventResponse.getDefaultInstance()))
              .setSchemaDescriptor(new WorkflowGrpcServiceMethodDescriptorSupplier("RequestReschedule"))
              .build();
        }
      }
    }
    return getRequestRescheduleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WorkflowGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceStub>() {
        @java.lang.Override
        public WorkflowGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkflowGrpcServiceStub(channel, callOptions);
        }
      };
    return WorkflowGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static WorkflowGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public WorkflowGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkflowGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return WorkflowGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WorkflowGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceBlockingStub>() {
        @java.lang.Override
        public WorkflowGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkflowGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return WorkflowGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WorkflowGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WorkflowGrpcServiceFutureStub>() {
        @java.lang.Override
        public WorkflowGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WorkflowGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return WorkflowGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void startAppointmentWorkflow(medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getStartAppointmentWorkflowMethod(), responseObserver);
    }

    /**
     */
    default void getWorkflowStatus(medlink.grpc.workflow.v1.GetWorkflowStatusRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.GetWorkflowStatusResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetWorkflowStatusMethod(), responseObserver);
    }

    /**
     */
    default void cancelAppointmentWorkflow(medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelAppointmentWorkflowMethod(), responseObserver);
    }

    /**
     */
    default void requestReschedule(medlink.grpc.workflow.v1.RequestRescheduleRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRequestRescheduleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WorkflowGrpcService.
   */
  public static abstract class WorkflowGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return WorkflowGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WorkflowGrpcService.
   */
  public static final class WorkflowGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WorkflowGrpcServiceStub> {
    private WorkflowGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkflowGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkflowGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void startAppointmentWorkflow(medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getStartAppointmentWorkflowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getWorkflowStatus(medlink.grpc.workflow.v1.GetWorkflowStatusRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.GetWorkflowStatusResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetWorkflowStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelAppointmentWorkflow(medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelAppointmentWorkflowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void requestReschedule(medlink.grpc.workflow.v1.RequestRescheduleRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRequestRescheduleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WorkflowGrpcService.
   */
  public static final class WorkflowGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<WorkflowGrpcServiceBlockingV2Stub> {
    private WorkflowGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkflowGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkflowGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse startAppointmentWorkflow(medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getStartAppointmentWorkflowMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.GetWorkflowStatusResponse getWorkflowStatus(medlink.grpc.workflow.v1.GetWorkflowStatusRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetWorkflowStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.WorkflowEventResponse cancelAppointmentWorkflow(medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCancelAppointmentWorkflowMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.WorkflowEventResponse requestReschedule(medlink.grpc.workflow.v1.RequestRescheduleRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRequestRescheduleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service WorkflowGrpcService.
   */
  public static final class WorkflowGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WorkflowGrpcServiceBlockingStub> {
    private WorkflowGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkflowGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkflowGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse startAppointmentWorkflow(medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getStartAppointmentWorkflowMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.GetWorkflowStatusResponse getWorkflowStatus(medlink.grpc.workflow.v1.GetWorkflowStatusRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetWorkflowStatusMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.WorkflowEventResponse cancelAppointmentWorkflow(medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelAppointmentWorkflowMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.workflow.v1.WorkflowEventResponse requestReschedule(medlink.grpc.workflow.v1.RequestRescheduleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRequestRescheduleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WorkflowGrpcService.
   */
  public static final class WorkflowGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WorkflowGrpcServiceFutureStub> {
    private WorkflowGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WorkflowGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WorkflowGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse> startAppointmentWorkflow(
        medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getStartAppointmentWorkflowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.workflow.v1.GetWorkflowStatusResponse> getWorkflowStatus(
        medlink.grpc.workflow.v1.GetWorkflowStatusRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetWorkflowStatusMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.workflow.v1.WorkflowEventResponse> cancelAppointmentWorkflow(
        medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelAppointmentWorkflowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.workflow.v1.WorkflowEventResponse> requestReschedule(
        medlink.grpc.workflow.v1.RequestRescheduleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRequestRescheduleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START_APPOINTMENT_WORKFLOW = 0;
  private static final int METHODID_GET_WORKFLOW_STATUS = 1;
  private static final int METHODID_CANCEL_APPOINTMENT_WORKFLOW = 2;
  private static final int METHODID_REQUEST_RESCHEDULE = 3;

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
        case METHODID_START_APPOINTMENT_WORKFLOW:
          serviceImpl.startAppointmentWorkflow((medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse>) responseObserver);
          break;
        case METHODID_GET_WORKFLOW_STATUS:
          serviceImpl.getWorkflowStatus((medlink.grpc.workflow.v1.GetWorkflowStatusRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.GetWorkflowStatusResponse>) responseObserver);
          break;
        case METHODID_CANCEL_APPOINTMENT_WORKFLOW:
          serviceImpl.cancelAppointmentWorkflow((medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse>) responseObserver);
          break;
        case METHODID_REQUEST_RESCHEDULE:
          serviceImpl.requestReschedule((medlink.grpc.workflow.v1.RequestRescheduleRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.workflow.v1.WorkflowEventResponse>) responseObserver);
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
          getStartAppointmentWorkflowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.workflow.v1.StartAppointmentWorkflowRequest,
              medlink.grpc.workflow.v1.StartAppointmentWorkflowResponse>(
                service, METHODID_START_APPOINTMENT_WORKFLOW)))
        .addMethod(
          getGetWorkflowStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.workflow.v1.GetWorkflowStatusRequest,
              medlink.grpc.workflow.v1.GetWorkflowStatusResponse>(
                service, METHODID_GET_WORKFLOW_STATUS)))
        .addMethod(
          getCancelAppointmentWorkflowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.workflow.v1.CancelAppointmentWorkflowRequest,
              medlink.grpc.workflow.v1.WorkflowEventResponse>(
                service, METHODID_CANCEL_APPOINTMENT_WORKFLOW)))
        .addMethod(
          getRequestRescheduleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.workflow.v1.RequestRescheduleRequest,
              medlink.grpc.workflow.v1.WorkflowEventResponse>(
                service, METHODID_REQUEST_RESCHEDULE)))
        .build();
  }

  private static abstract class WorkflowGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WorkflowGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.grpc.workflow.v1.Workflow.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WorkflowGrpcService");
    }
  }

  private static final class WorkflowGrpcServiceFileDescriptorSupplier
      extends WorkflowGrpcServiceBaseDescriptorSupplier {
    WorkflowGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class WorkflowGrpcServiceMethodDescriptorSupplier
      extends WorkflowGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    WorkflowGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (WorkflowGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WorkflowGrpcServiceFileDescriptorSupplier())
              .addMethod(getStartAppointmentWorkflowMethod())
              .addMethod(getGetWorkflowStatusMethod())
              .addMethod(getCancelAppointmentWorkflowMethod())
              .addMethod(getRequestRescheduleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
