package medlink.project.grpc.rendezvous;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class RendezVousGrpcServiceGrpc {

  private RendezVousGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.RendezVousGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCreateRendezVousMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateRendezVous",
      requestType = medlink.project.grpc.rendezvous.CreateRendezVousRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCreateRendezVousMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getCreateRendezVousMethod;
    if ((getCreateRendezVousMethod = RendezVousGrpcServiceGrpc.getCreateRendezVousMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getCreateRendezVousMethod = RendezVousGrpcServiceGrpc.getCreateRendezVousMethod) == null) {
          RendezVousGrpcServiceGrpc.getCreateRendezVousMethod = getCreateRendezVousMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.CreateRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateRendezVous"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.CreateRendezVousRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("CreateRendezVous"))
              .build();
        }
      }
    }
    return getCreateRendezVousMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.GetRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getGetRendezVousMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetRendezVous",
      requestType = medlink.project.grpc.rendezvous.GetRendezVousRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.GetRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getGetRendezVousMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.GetRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getGetRendezVousMethod;
    if ((getGetRendezVousMethod = RendezVousGrpcServiceGrpc.getGetRendezVousMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getGetRendezVousMethod = RendezVousGrpcServiceGrpc.getGetRendezVousMethod) == null) {
          RendezVousGrpcServiceGrpc.getGetRendezVousMethod = getGetRendezVousMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.GetRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetRendezVous"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.GetRendezVousRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("GetRendezVous"))
              .build();
        }
      }
    }
    return getGetRendezVousMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CancelRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCancelRendezVousMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CancelRendezVous",
      requestType = medlink.project.grpc.rendezvous.CancelRendezVousRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CancelRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCancelRendezVousMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CancelRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getCancelRendezVousMethod;
    if ((getCancelRendezVousMethod = RendezVousGrpcServiceGrpc.getCancelRendezVousMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getCancelRendezVousMethod = RendezVousGrpcServiceGrpc.getCancelRendezVousMethod) == null) {
          RendezVousGrpcServiceGrpc.getCancelRendezVousMethod = getCancelRendezVousMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.CancelRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CancelRendezVous"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.CancelRendezVousRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("CancelRendezVous"))
              .build();
        }
      }
    }
    return getCancelRendezVousMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.RescheduleRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getRescheduleRendezVousMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RescheduleRendezVous",
      requestType = medlink.project.grpc.rendezvous.RescheduleRendezVousRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.RescheduleRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getRescheduleRendezVousMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.RescheduleRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getRescheduleRendezVousMethod;
    if ((getRescheduleRendezVousMethod = RendezVousGrpcServiceGrpc.getRescheduleRendezVousMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getRescheduleRendezVousMethod = RendezVousGrpcServiceGrpc.getRescheduleRendezVousMethod) == null) {
          RendezVousGrpcServiceGrpc.getRescheduleRendezVousMethod = getRescheduleRendezVousMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.RescheduleRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RescheduleRendezVous"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RescheduleRendezVousRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("RescheduleRendezVous"))
              .build();
        }
      }
    }
    return getRescheduleRendezVousMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.UpdatePresenceRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getUpdatePresenceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdatePresence",
      requestType = medlink.project.grpc.rendezvous.UpdatePresenceRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.UpdatePresenceRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getUpdatePresenceMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.UpdatePresenceRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getUpdatePresenceMethod;
    if ((getUpdatePresenceMethod = RendezVousGrpcServiceGrpc.getUpdatePresenceMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getUpdatePresenceMethod = RendezVousGrpcServiceGrpc.getUpdatePresenceMethod) == null) {
          RendezVousGrpcServiceGrpc.getUpdatePresenceMethod = getUpdatePresenceMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.UpdatePresenceRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdatePresence"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.UpdatePresenceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("UpdatePresence"))
              .build();
        }
      }
    }
    return getUpdatePresenceMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CompleteConsultationRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCompleteConsultationMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CompleteConsultation",
      requestType = medlink.project.grpc.rendezvous.CompleteConsultationRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CompleteConsultationRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCompleteConsultationMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CompleteConsultationRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getCompleteConsultationMethod;
    if ((getCompleteConsultationMethod = RendezVousGrpcServiceGrpc.getCompleteConsultationMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getCompleteConsultationMethod = RendezVousGrpcServiceGrpc.getCompleteConsultationMethod) == null) {
          RendezVousGrpcServiceGrpc.getCompleteConsultationMethod = getCompleteConsultationMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.CompleteConsultationRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CompleteConsultation"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.CompleteConsultationRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("CompleteConsultation"))
              .build();
        }
      }
    }
    return getCompleteConsultationMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCreateFollowUpRendezVousMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateFollowUpRendezVous",
      requestType = medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest.class,
      responseType = medlink.project.grpc.rendezvous.RendezVousResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest,
      medlink.project.grpc.rendezvous.RendezVousResponse> getCreateFollowUpRendezVousMethod() {
    io.grpc.MethodDescriptor<medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse> getCreateFollowUpRendezVousMethod;
    if ((getCreateFollowUpRendezVousMethod = RendezVousGrpcServiceGrpc.getCreateFollowUpRendezVousMethod) == null) {
      synchronized (RendezVousGrpcServiceGrpc.class) {
        if ((getCreateFollowUpRendezVousMethod = RendezVousGrpcServiceGrpc.getCreateFollowUpRendezVousMethod) == null) {
          RendezVousGrpcServiceGrpc.getCreateFollowUpRendezVousMethod = getCreateFollowUpRendezVousMethod =
              io.grpc.MethodDescriptor.<medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest, medlink.project.grpc.rendezvous.RendezVousResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateFollowUpRendezVous"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.project.grpc.rendezvous.RendezVousResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RendezVousGrpcServiceMethodDescriptorSupplier("CreateFollowUpRendezVous"))
              .build();
        }
      }
    }
    return getCreateFollowUpRendezVousMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RendezVousGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceStub>() {
        @java.lang.Override
        public RendezVousGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RendezVousGrpcServiceStub(channel, callOptions);
        }
      };
    return RendezVousGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static RendezVousGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public RendezVousGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RendezVousGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return RendezVousGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RendezVousGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceBlockingStub>() {
        @java.lang.Override
        public RendezVousGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RendezVousGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return RendezVousGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RendezVousGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RendezVousGrpcServiceFutureStub>() {
        @java.lang.Override
        public RendezVousGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RendezVousGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return RendezVousGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void createRendezVous(medlink.project.grpc.rendezvous.CreateRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateRendezVousMethod(), responseObserver);
    }

    /**
     */
    default void getRendezVous(medlink.project.grpc.rendezvous.GetRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetRendezVousMethod(), responseObserver);
    }

    /**
     */
    default void cancelRendezVous(medlink.project.grpc.rendezvous.CancelRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCancelRendezVousMethod(), responseObserver);
    }

    /**
     */
    default void rescheduleRendezVous(medlink.project.grpc.rendezvous.RescheduleRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRescheduleRendezVousMethod(), responseObserver);
    }

    /**
     */
    default void updatePresence(medlink.project.grpc.rendezvous.UpdatePresenceRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdatePresenceMethod(), responseObserver);
    }

    /**
     */
    default void completeConsultation(medlink.project.grpc.rendezvous.CompleteConsultationRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCompleteConsultationMethod(), responseObserver);
    }

    /**
     */
    default void createFollowUpRendezVous(medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateFollowUpRendezVousMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RendezVousGrpcService.
   */
  public static abstract class RendezVousGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RendezVousGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RendezVousGrpcService.
   */
  public static final class RendezVousGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RendezVousGrpcServiceStub> {
    private RendezVousGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RendezVousGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RendezVousGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void createRendezVous(medlink.project.grpc.rendezvous.CreateRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateRendezVousMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getRendezVous(medlink.project.grpc.rendezvous.GetRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetRendezVousMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void cancelRendezVous(medlink.project.grpc.rendezvous.CancelRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCancelRendezVousMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rescheduleRendezVous(medlink.project.grpc.rendezvous.RescheduleRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRescheduleRendezVousMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updatePresence(medlink.project.grpc.rendezvous.UpdatePresenceRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdatePresenceMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void completeConsultation(medlink.project.grpc.rendezvous.CompleteConsultationRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCompleteConsultationMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createFollowUpRendezVous(medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest request,
        io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateFollowUpRendezVousMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RendezVousGrpcService.
   */
  public static final class RendezVousGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<RendezVousGrpcServiceBlockingV2Stub> {
    private RendezVousGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RendezVousGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RendezVousGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse createRendezVous(medlink.project.grpc.rendezvous.CreateRendezVousRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse getRendezVous(medlink.project.grpc.rendezvous.GetRendezVousRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse cancelRendezVous(medlink.project.grpc.rendezvous.CancelRendezVousRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCancelRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse rescheduleRendezVous(medlink.project.grpc.rendezvous.RescheduleRendezVousRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRescheduleRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse updatePresence(medlink.project.grpc.rendezvous.UpdatePresenceRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdatePresenceMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse completeConsultation(medlink.project.grpc.rendezvous.CompleteConsultationRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCompleteConsultationMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse createFollowUpRendezVous(medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateFollowUpRendezVousMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service RendezVousGrpcService.
   */
  public static final class RendezVousGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RendezVousGrpcServiceBlockingStub> {
    private RendezVousGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RendezVousGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RendezVousGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse createRendezVous(medlink.project.grpc.rendezvous.CreateRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse getRendezVous(medlink.project.grpc.rendezvous.GetRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse cancelRendezVous(medlink.project.grpc.rendezvous.CancelRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCancelRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse rescheduleRendezVous(medlink.project.grpc.rendezvous.RescheduleRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRescheduleRendezVousMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse updatePresence(medlink.project.grpc.rendezvous.UpdatePresenceRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdatePresenceMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse completeConsultation(medlink.project.grpc.rendezvous.CompleteConsultationRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCompleteConsultationMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.project.grpc.rendezvous.RendezVousResponse createFollowUpRendezVous(medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateFollowUpRendezVousMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RendezVousGrpcService.
   */
  public static final class RendezVousGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RendezVousGrpcServiceFutureStub> {
    private RendezVousGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RendezVousGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RendezVousGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> createRendezVous(
        medlink.project.grpc.rendezvous.CreateRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateRendezVousMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> getRendezVous(
        medlink.project.grpc.rendezvous.GetRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetRendezVousMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> cancelRendezVous(
        medlink.project.grpc.rendezvous.CancelRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCancelRendezVousMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> rescheduleRendezVous(
        medlink.project.grpc.rendezvous.RescheduleRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRescheduleRendezVousMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> updatePresence(
        medlink.project.grpc.rendezvous.UpdatePresenceRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdatePresenceMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> completeConsultation(
        medlink.project.grpc.rendezvous.CompleteConsultationRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCompleteConsultationMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.project.grpc.rendezvous.RendezVousResponse> createFollowUpRendezVous(
        medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateFollowUpRendezVousMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_RENDEZ_VOUS = 0;
  private static final int METHODID_GET_RENDEZ_VOUS = 1;
  private static final int METHODID_CANCEL_RENDEZ_VOUS = 2;
  private static final int METHODID_RESCHEDULE_RENDEZ_VOUS = 3;
  private static final int METHODID_UPDATE_PRESENCE = 4;
  private static final int METHODID_COMPLETE_CONSULTATION = 5;
  private static final int METHODID_CREATE_FOLLOW_UP_RENDEZ_VOUS = 6;

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
        case METHODID_CREATE_RENDEZ_VOUS:
          serviceImpl.createRendezVous((medlink.project.grpc.rendezvous.CreateRendezVousRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_GET_RENDEZ_VOUS:
          serviceImpl.getRendezVous((medlink.project.grpc.rendezvous.GetRendezVousRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_CANCEL_RENDEZ_VOUS:
          serviceImpl.cancelRendezVous((medlink.project.grpc.rendezvous.CancelRendezVousRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_RESCHEDULE_RENDEZ_VOUS:
          serviceImpl.rescheduleRendezVous((medlink.project.grpc.rendezvous.RescheduleRendezVousRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_UPDATE_PRESENCE:
          serviceImpl.updatePresence((medlink.project.grpc.rendezvous.UpdatePresenceRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_COMPLETE_CONSULTATION:
          serviceImpl.completeConsultation((medlink.project.grpc.rendezvous.CompleteConsultationRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
          break;
        case METHODID_CREATE_FOLLOW_UP_RENDEZ_VOUS:
          serviceImpl.createFollowUpRendezVous((medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest) request,
              (io.grpc.stub.StreamObserver<medlink.project.grpc.rendezvous.RendezVousResponse>) responseObserver);
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
          getCreateRendezVousMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.CreateRendezVousRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_CREATE_RENDEZ_VOUS)))
        .addMethod(
          getGetRendezVousMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.GetRendezVousRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_GET_RENDEZ_VOUS)))
        .addMethod(
          getCancelRendezVousMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.CancelRendezVousRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_CANCEL_RENDEZ_VOUS)))
        .addMethod(
          getRescheduleRendezVousMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.RescheduleRendezVousRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_RESCHEDULE_RENDEZ_VOUS)))
        .addMethod(
          getUpdatePresenceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.UpdatePresenceRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_UPDATE_PRESENCE)))
        .addMethod(
          getCompleteConsultationMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.CompleteConsultationRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_COMPLETE_CONSULTATION)))
        .addMethod(
          getCreateFollowUpRendezVousMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.project.grpc.rendezvous.CreateFollowUpRendezVousRequest,
              medlink.project.grpc.rendezvous.RendezVousResponse>(
                service, METHODID_CREATE_FOLLOW_UP_RENDEZ_VOUS)))
        .build();
  }

  private static abstract class RendezVousGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RendezVousGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.project.grpc.rendezvous.RendezVousProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RendezVousGrpcService");
    }
  }

  private static final class RendezVousGrpcServiceFileDescriptorSupplier
      extends RendezVousGrpcServiceBaseDescriptorSupplier {
    RendezVousGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class RendezVousGrpcServiceMethodDescriptorSupplier
      extends RendezVousGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RendezVousGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RendezVousGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RendezVousGrpcServiceFileDescriptorSupplier())
              .addMethod(getCreateRendezVousMethod())
              .addMethod(getGetRendezVousMethod())
              .addMethod(getCancelRendezVousMethod())
              .addMethod(getRescheduleRendezVousMethod())
              .addMethod(getUpdatePresenceMethod())
              .addMethod(getCompleteConsultationMethod())
              .addMethod(getCreateFollowUpRendezVousMethod())
              .build();
        }
      }
    }
    return result;
  }
}
