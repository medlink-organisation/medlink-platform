package medlink.grpc.auth.v1;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthGrpcServiceGrpc {

  private AuthGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "medilink.v1.AuthGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetUserContactRequest,
      medlink.grpc.auth.v1.UserContactResponse> getGetUserContactMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUserContact",
      requestType = medlink.grpc.auth.v1.GetUserContactRequest.class,
      responseType = medlink.grpc.auth.v1.UserContactResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetUserContactRequest,
      medlink.grpc.auth.v1.UserContactResponse> getGetUserContactMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetUserContactRequest, medlink.grpc.auth.v1.UserContactResponse> getGetUserContactMethod;
    if ((getGetUserContactMethod = AuthGrpcServiceGrpc.getGetUserContactMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getGetUserContactMethod = AuthGrpcServiceGrpc.getGetUserContactMethod) == null) {
          AuthGrpcServiceGrpc.getGetUserContactMethod = getGetUserContactMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetUserContactRequest, medlink.grpc.auth.v1.UserContactResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUserContact"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetUserContactRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.UserContactResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("GetUserContact"))
              .build();
        }
      }
    }
    return getGetUserContactMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getGetPatientByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPatientById",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.PatientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getGetPatientByIdMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse> getGetPatientByIdMethod;
    if ((getGetPatientByIdMethod = AuthGrpcServiceGrpc.getGetPatientByIdMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getGetPatientByIdMethod = AuthGrpcServiceGrpc.getGetPatientByIdMethod) == null) {
          AuthGrpcServiceGrpc.getGetPatientByIdMethod = getGetPatientByIdMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPatientById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("GetPatientById"))
              .build();
        }
      }
    }
    return getGetPatientByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientsRequest,
      medlink.grpc.auth.v1.PatientPageResponse> getGetAllPatientsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllPatients",
      requestType = medlink.grpc.auth.v1.GetPatientsRequest.class,
      responseType = medlink.grpc.auth.v1.PatientPageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientsRequest,
      medlink.grpc.auth.v1.PatientPageResponse> getGetAllPatientsMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientsRequest, medlink.grpc.auth.v1.PatientPageResponse> getGetAllPatientsMethod;
    if ((getGetAllPatientsMethod = AuthGrpcServiceGrpc.getGetAllPatientsMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getGetAllPatientsMethod = AuthGrpcServiceGrpc.getGetAllPatientsMethod) == null) {
          AuthGrpcServiceGrpc.getGetAllPatientsMethod = getGetAllPatientsMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientsRequest, medlink.grpc.auth.v1.PatientPageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllPatients"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientPageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("GetAllPatients"))
              .build();
        }
      }
    }
    return getGetAllPatientsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest,
      medlink.grpc.auth.v1.PatientResponse> getGetPatientByEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPatientByEmail",
      requestType = medlink.grpc.auth.v1.GetPatientByEmailRequest.class,
      responseType = medlink.grpc.auth.v1.PatientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest,
      medlink.grpc.auth.v1.PatientResponse> getGetPatientByEmailMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest, medlink.grpc.auth.v1.PatientResponse> getGetPatientByEmailMethod;
    if ((getGetPatientByEmailMethod = AuthGrpcServiceGrpc.getGetPatientByEmailMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getGetPatientByEmailMethod = AuthGrpcServiceGrpc.getGetPatientByEmailMethod) == null) {
          AuthGrpcServiceGrpc.getGetPatientByEmailMethod = getGetPatientByEmailMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByEmailRequest, medlink.grpc.auth.v1.PatientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPatientByEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByEmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("GetPatientByEmail"))
              .build();
        }
      }
    }
    return getGetPatientByEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExistsPatientById",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.BooleanResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByIdMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByIdMethod;
    if ((getExistsPatientByIdMethod = AuthGrpcServiceGrpc.getExistsPatientByIdMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getExistsPatientByIdMethod = AuthGrpcServiceGrpc.getExistsPatientByIdMethod) == null) {
          AuthGrpcServiceGrpc.getExistsPatientByIdMethod = getExistsPatientByIdMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.BooleanResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExistsPatientById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.BooleanResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("ExistsPatientById"))
              .build();
        }
      }
    }
    return getExistsPatientByIdMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest,
      medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByEmailMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ExistsPatientByEmail",
      requestType = medlink.grpc.auth.v1.GetPatientByEmailRequest.class,
      responseType = medlink.grpc.auth.v1.BooleanResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest,
      medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByEmailMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByEmailRequest, medlink.grpc.auth.v1.BooleanResponse> getExistsPatientByEmailMethod;
    if ((getExistsPatientByEmailMethod = AuthGrpcServiceGrpc.getExistsPatientByEmailMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getExistsPatientByEmailMethod = AuthGrpcServiceGrpc.getExistsPatientByEmailMethod) == null) {
          AuthGrpcServiceGrpc.getExistsPatientByEmailMethod = getExistsPatientByEmailMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByEmailRequest, medlink.grpc.auth.v1.BooleanResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ExistsPatientByEmail"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByEmailRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.BooleanResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("ExistsPatientByEmail"))
              .build();
        }
      }
    }
    return getExistsPatientByEmailMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.BooleanResponse> getIsPatientActiveMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "IsPatientActive",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.BooleanResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.BooleanResponse> getIsPatientActiveMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.BooleanResponse> getIsPatientActiveMethod;
    if ((getIsPatientActiveMethod = AuthGrpcServiceGrpc.getIsPatientActiveMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getIsPatientActiveMethod = AuthGrpcServiceGrpc.getIsPatientActiveMethod) == null) {
          AuthGrpcServiceGrpc.getIsPatientActiveMethod = getIsPatientActiveMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.BooleanResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "IsPatientActive"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.BooleanResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("IsPatientActive"))
              .build();
        }
      }
    }
    return getIsPatientActiveMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getEnablePatientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "EnablePatient",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.PatientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getEnablePatientMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse> getEnablePatientMethod;
    if ((getEnablePatientMethod = AuthGrpcServiceGrpc.getEnablePatientMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getEnablePatientMethod = AuthGrpcServiceGrpc.getEnablePatientMethod) == null) {
          AuthGrpcServiceGrpc.getEnablePatientMethod = getEnablePatientMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "EnablePatient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("EnablePatient"))
              .build();
        }
      }
    }
    return getEnablePatientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getDisablePatientMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DisablePatient",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.PatientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientResponse> getDisablePatientMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse> getDisablePatientMethod;
    if ((getDisablePatientMethod = AuthGrpcServiceGrpc.getDisablePatientMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getDisablePatientMethod = AuthGrpcServiceGrpc.getDisablePatientMethod) == null) {
          AuthGrpcServiceGrpc.getDisablePatientMethod = getDisablePatientMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DisablePatient"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("DisablePatient"))
              .build();
        }
      }
    }
    return getDisablePatientMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientRolesResponse> getGetPatientRolesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPatientRoles",
      requestType = medlink.grpc.auth.v1.GetPatientByIdRequest.class,
      responseType = medlink.grpc.auth.v1.PatientRolesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest,
      medlink.grpc.auth.v1.PatientRolesResponse> getGetPatientRolesMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientRolesResponse> getGetPatientRolesMethod;
    if ((getGetPatientRolesMethod = AuthGrpcServiceGrpc.getGetPatientRolesMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getGetPatientRolesMethod = AuthGrpcServiceGrpc.getGetPatientRolesMethod) == null) {
          AuthGrpcServiceGrpc.getGetPatientRolesMethod = getGetPatientRolesMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.GetPatientByIdRequest, medlink.grpc.auth.v1.PatientRolesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPatientRoles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.GetPatientByIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientRolesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("GetPatientRoles"))
              .build();
        }
      }
    }
    return getGetPatientRolesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<medlink.grpc.auth.v1.UpdatePatientContactRequest,
      medlink.grpc.auth.v1.PatientResponse> getUpdatePatientContactMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdatePatientContact",
      requestType = medlink.grpc.auth.v1.UpdatePatientContactRequest.class,
      responseType = medlink.grpc.auth.v1.PatientResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<medlink.grpc.auth.v1.UpdatePatientContactRequest,
      medlink.grpc.auth.v1.PatientResponse> getUpdatePatientContactMethod() {
    io.grpc.MethodDescriptor<medlink.grpc.auth.v1.UpdatePatientContactRequest, medlink.grpc.auth.v1.PatientResponse> getUpdatePatientContactMethod;
    if ((getUpdatePatientContactMethod = AuthGrpcServiceGrpc.getUpdatePatientContactMethod) == null) {
      synchronized (AuthGrpcServiceGrpc.class) {
        if ((getUpdatePatientContactMethod = AuthGrpcServiceGrpc.getUpdatePatientContactMethod) == null) {
          AuthGrpcServiceGrpc.getUpdatePatientContactMethod = getUpdatePatientContactMethod =
              io.grpc.MethodDescriptor.<medlink.grpc.auth.v1.UpdatePatientContactRequest, medlink.grpc.auth.v1.PatientResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdatePatientContact"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.UpdatePatientContactRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  medlink.grpc.auth.v1.PatientResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthGrpcServiceMethodDescriptorSupplier("UpdatePatientContact"))
              .build();
        }
      }
    }
    return getUpdatePatientContactMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceStub>() {
        @java.lang.Override
        public AuthGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthGrpcServiceStub(channel, callOptions);
        }
      };
    return AuthGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static AuthGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public AuthGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return AuthGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceBlockingStub>() {
        @java.lang.Override
        public AuthGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthGrpcServiceFutureStub>() {
        @java.lang.Override
        public AuthGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return AuthGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getUserContact(medlink.grpc.auth.v1.GetUserContactRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.UserContactResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUserContactMethod(), responseObserver);
    }

    /**
     */
    default void getPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPatientByIdMethod(), responseObserver);
    }

    /**
     */
    default void getAllPatients(medlink.grpc.auth.v1.GetPatientsRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientPageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllPatientsMethod(), responseObserver);
    }

    /**
     */
    default void getPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPatientByEmailMethod(), responseObserver);
    }

    /**
     */
    default void existsPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExistsPatientByIdMethod(), responseObserver);
    }

    /**
     */
    default void existsPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExistsPatientByEmailMethod(), responseObserver);
    }

    /**
     */
    default void isPatientActive(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsPatientActiveMethod(), responseObserver);
    }

    /**
     */
    default void enablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEnablePatientMethod(), responseObserver);
    }

    /**
     */
    default void disablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDisablePatientMethod(), responseObserver);
    }

    /**
     */
    default void getPatientRoles(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientRolesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPatientRolesMethod(), responseObserver);
    }

    /**
     */
    default void updatePatientContact(medlink.grpc.auth.v1.UpdatePatientContactRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdatePatientContactMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthGrpcService.
   */
  public static abstract class AuthGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthGrpcService.
   */
  public static final class AuthGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthGrpcServiceStub> {
    private AuthGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getUserContact(medlink.grpc.auth.v1.GetUserContactRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.UserContactResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUserContactMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPatientByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllPatients(medlink.grpc.auth.v1.GetPatientsRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientPageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllPatientsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPatientByEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void existsPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExistsPatientByIdMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void existsPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExistsPatientByEmailMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void isPatientActive(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsPatientActiveMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void enablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEnablePatientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void disablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDisablePatientMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPatientRoles(medlink.grpc.auth.v1.GetPatientByIdRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientRolesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPatientRolesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updatePatientContact(medlink.grpc.auth.v1.UpdatePatientContactRequest request,
        io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdatePatientContactMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthGrpcService.
   */
  public static final class AuthGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<AuthGrpcServiceBlockingV2Stub> {
    private AuthGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.auth.v1.UserContactResponse getUserContact(medlink.grpc.auth.v1.GetUserContactRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetUserContactMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse getPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetPatientByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientPageResponse getAllPatients(medlink.grpc.auth.v1.GetPatientsRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetAllPatientsMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse getPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetPatientByEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse existsPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getExistsPatientByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse existsPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getExistsPatientByEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse isPatientActive(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getIsPatientActiveMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse enablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getEnablePatientMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse disablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getDisablePatientMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientRolesResponse getPatientRoles(medlink.grpc.auth.v1.GetPatientByIdRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetPatientRolesMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse updatePatientContact(medlink.grpc.auth.v1.UpdatePatientContactRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getUpdatePatientContactMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service AuthGrpcService.
   */
  public static final class AuthGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthGrpcServiceBlockingStub> {
    private AuthGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public medlink.grpc.auth.v1.UserContactResponse getUserContact(medlink.grpc.auth.v1.GetUserContactRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUserContactMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse getPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPatientByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientPageResponse getAllPatients(medlink.grpc.auth.v1.GetPatientsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllPatientsMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse getPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPatientByEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse existsPatientById(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExistsPatientByIdMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse existsPatientByEmail(medlink.grpc.auth.v1.GetPatientByEmailRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExistsPatientByEmailMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.BooleanResponse isPatientActive(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsPatientActiveMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse enablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEnablePatientMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse disablePatient(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDisablePatientMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientRolesResponse getPatientRoles(medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPatientRolesMethod(), getCallOptions(), request);
    }

    /**
     */
    public medlink.grpc.auth.v1.PatientResponse updatePatientContact(medlink.grpc.auth.v1.UpdatePatientContactRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdatePatientContactMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthGrpcService.
   */
  public static final class AuthGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthGrpcServiceFutureStub> {
    private AuthGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.UserContactResponse> getUserContact(
        medlink.grpc.auth.v1.GetUserContactRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUserContactMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientResponse> getPatientById(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPatientByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientPageResponse> getAllPatients(
        medlink.grpc.auth.v1.GetPatientsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllPatientsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientResponse> getPatientByEmail(
        medlink.grpc.auth.v1.GetPatientByEmailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPatientByEmailMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.BooleanResponse> existsPatientById(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExistsPatientByIdMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.BooleanResponse> existsPatientByEmail(
        medlink.grpc.auth.v1.GetPatientByEmailRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExistsPatientByEmailMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.BooleanResponse> isPatientActive(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsPatientActiveMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientResponse> enablePatient(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEnablePatientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientResponse> disablePatient(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDisablePatientMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientRolesResponse> getPatientRoles(
        medlink.grpc.auth.v1.GetPatientByIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPatientRolesMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<medlink.grpc.auth.v1.PatientResponse> updatePatientContact(
        medlink.grpc.auth.v1.UpdatePatientContactRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdatePatientContactMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_USER_CONTACT = 0;
  private static final int METHODID_GET_PATIENT_BY_ID = 1;
  private static final int METHODID_GET_ALL_PATIENTS = 2;
  private static final int METHODID_GET_PATIENT_BY_EMAIL = 3;
  private static final int METHODID_EXISTS_PATIENT_BY_ID = 4;
  private static final int METHODID_EXISTS_PATIENT_BY_EMAIL = 5;
  private static final int METHODID_IS_PATIENT_ACTIVE = 6;
  private static final int METHODID_ENABLE_PATIENT = 7;
  private static final int METHODID_DISABLE_PATIENT = 8;
  private static final int METHODID_GET_PATIENT_ROLES = 9;
  private static final int METHODID_UPDATE_PATIENT_CONTACT = 10;

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
        case METHODID_GET_USER_CONTACT:
          serviceImpl.getUserContact((medlink.grpc.auth.v1.GetUserContactRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.UserContactResponse>) responseObserver);
          break;
        case METHODID_GET_PATIENT_BY_ID:
          serviceImpl.getPatientById((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_PATIENTS:
          serviceImpl.getAllPatients((medlink.grpc.auth.v1.GetPatientsRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientPageResponse>) responseObserver);
          break;
        case METHODID_GET_PATIENT_BY_EMAIL:
          serviceImpl.getPatientByEmail((medlink.grpc.auth.v1.GetPatientByEmailRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse>) responseObserver);
          break;
        case METHODID_EXISTS_PATIENT_BY_ID:
          serviceImpl.existsPatientById((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse>) responseObserver);
          break;
        case METHODID_EXISTS_PATIENT_BY_EMAIL:
          serviceImpl.existsPatientByEmail((medlink.grpc.auth.v1.GetPatientByEmailRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse>) responseObserver);
          break;
        case METHODID_IS_PATIENT_ACTIVE:
          serviceImpl.isPatientActive((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.BooleanResponse>) responseObserver);
          break;
        case METHODID_ENABLE_PATIENT:
          serviceImpl.enablePatient((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse>) responseObserver);
          break;
        case METHODID_DISABLE_PATIENT:
          serviceImpl.disablePatient((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse>) responseObserver);
          break;
        case METHODID_GET_PATIENT_ROLES:
          serviceImpl.getPatientRoles((medlink.grpc.auth.v1.GetPatientByIdRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientRolesResponse>) responseObserver);
          break;
        case METHODID_UPDATE_PATIENT_CONTACT:
          serviceImpl.updatePatientContact((medlink.grpc.auth.v1.UpdatePatientContactRequest) request,
              (io.grpc.stub.StreamObserver<medlink.grpc.auth.v1.PatientResponse>) responseObserver);
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
          getGetUserContactMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetUserContactRequest,
              medlink.grpc.auth.v1.UserContactResponse>(
                service, METHODID_GET_USER_CONTACT)))
        .addMethod(
          getGetPatientByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.PatientResponse>(
                service, METHODID_GET_PATIENT_BY_ID)))
        .addMethod(
          getGetAllPatientsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientsRequest,
              medlink.grpc.auth.v1.PatientPageResponse>(
                service, METHODID_GET_ALL_PATIENTS)))
        .addMethod(
          getGetPatientByEmailMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByEmailRequest,
              medlink.grpc.auth.v1.PatientResponse>(
                service, METHODID_GET_PATIENT_BY_EMAIL)))
        .addMethod(
          getExistsPatientByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.BooleanResponse>(
                service, METHODID_EXISTS_PATIENT_BY_ID)))
        .addMethod(
          getExistsPatientByEmailMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByEmailRequest,
              medlink.grpc.auth.v1.BooleanResponse>(
                service, METHODID_EXISTS_PATIENT_BY_EMAIL)))
        .addMethod(
          getIsPatientActiveMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.BooleanResponse>(
                service, METHODID_IS_PATIENT_ACTIVE)))
        .addMethod(
          getEnablePatientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.PatientResponse>(
                service, METHODID_ENABLE_PATIENT)))
        .addMethod(
          getDisablePatientMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.PatientResponse>(
                service, METHODID_DISABLE_PATIENT)))
        .addMethod(
          getGetPatientRolesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.GetPatientByIdRequest,
              medlink.grpc.auth.v1.PatientRolesResponse>(
                service, METHODID_GET_PATIENT_ROLES)))
        .addMethod(
          getUpdatePatientContactMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              medlink.grpc.auth.v1.UpdatePatientContactRequest,
              medlink.grpc.auth.v1.PatientResponse>(
                service, METHODID_UPDATE_PATIENT_CONTACT)))
        .build();
  }

  private static abstract class AuthGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return medlink.grpc.auth.v1.Auth.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthGrpcService");
    }
  }

  private static final class AuthGrpcServiceFileDescriptorSupplier
      extends AuthGrpcServiceBaseDescriptorSupplier {
    AuthGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class AuthGrpcServiceMethodDescriptorSupplier
      extends AuthGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AuthGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetUserContactMethod())
              .addMethod(getGetPatientByIdMethod())
              .addMethod(getGetAllPatientsMethod())
              .addMethod(getGetPatientByEmailMethod())
              .addMethod(getExistsPatientByIdMethod())
              .addMethod(getExistsPatientByEmailMethod())
              .addMethod(getIsPatientActiveMethod())
              .addMethod(getEnablePatientMethod())
              .addMethod(getDisablePatientMethod())
              .addMethod(getGetPatientRolesMethod())
              .addMethod(getUpdatePatientContactMethod())
              .build();
        }
      }
    }
    return result;
  }
}
