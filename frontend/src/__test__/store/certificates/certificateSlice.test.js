import axios from 'axios';
import {
  certificatesReducer,
  changePageSize,
  changeStatus,
  createCertificate,
  deleteCertificate,
  loadCertificates,
  resetCertificates,
  setPage,
  setPageByUrl,
  updateCertificate,
} from '../../../store/certificates/certificateSlice';
import {certificateArray, createCertificateState, currentPage} from './testData';

jest.mock('axios');
const certificateByIdUrl = jest.fn().mockRejectedValue('certificates/1');
const dispatch = jest.fn();

describe('load certificates async actions', () => {
  const certificatesUrl = jest.fn().mockRejectedValue('certificates');
  const data = {
    number: 0,
    size: 10,
    filter: {},
  };

  test('load certificates with infinite scroll', async () => {
    axios.post.mockReturnValue(certificateArray);
    const thunk = loadCertificates(data);

    await thunk(dispatch, null, {client: axios, api: {certificatesUrl}});
    const [pending, fulfilled] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadCertificates.pending().type);
    expect(fulfilled[0].type).toBe(loadCertificates.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({
      response: certificateArray,
      infinite: true,
    });
  });

  test('load certificates without infinite scroll', async () => {
    axios.post.mockReturnValue(certificateArray);

    const thunk = loadCertificates({
      ...data,
      infinite: false,
    });
    await thunk(dispatch, null, {client: axios, api: {certificatesUrl}});

    const actual = dispatch.mock.calls[1][0].payload;
    const expected = {
      response: certificateArray,
      infinite: false,
    };

    expect(actual).toEqual(expected);
  });

  test('load certificates fail with handled error', async () => {
    const e = {
      response: {
        data: {
          errorMessage: 'This is error',
        },
      },
    };
    axios.post.mockRejectedValue(e);

    const thunk = loadCertificates(data);
    await thunk(dispatch, null, {client: axios, api: {certificatesUrl}});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadCertificates.pending().type);
    expect(rejected[0].type).toBe(loadCertificates.rejected().type);
    expect(rejected[0].payload).toBe('This is error');
  });

  test('load certificates fail', async () => {
    axios.post.mockRejectedValue(new Error('This is error'));

    const thunk = loadCertificates(data);
    await thunk(dispatch, null, {client: axios, api: {certificatesUrl}});
    const actual = dispatch.mock.calls[1][0].payload;

    expect(actual).toBe('This is error');
  });
});

describe('create certificate async actions', () => {
  const api = {CREATE_CERTIFICATE: '/certificate/create'};

  test('create certificate', async () => {
    axios.post.mockReturnValue({id: 1, name: 'New Certificate'});

    const thunk = createCertificate();
    await thunk(dispatch, null, {client: axios, api});
    const [pending, fulfilled] = dispatch.mock.calls;
    const headers = axios.post.mock.calls[0][2].headers;

    expect(headers['Content-Type']).toBe('multipart/form-data');
    expect(pending[0].type).toBe(createCertificate.pending().type);
    expect(fulfilled[0].type).toBe(createCertificate.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({
      id: 1,
      name: 'New Certificate',
    });
  });

  test('create certificate fail', async () => {
    axios.post.mockRejectedValue(new Error('This is error'));

    const thunk = createCertificate();
    await thunk(dispatch, null, {client: axios, api});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(createCertificate.pending().type);
    expect(rejected[0].type).toBe(createCertificate.rejected().type);
    expect(rejected[0].payload).toBe('This is error');
  });
});

describe('update certificate async actions', () => {
  const data = {get: () => 1};

  test('update certificate', async () => {
    axios.patch.mockReturnValue({id: 1, name: 'Updated Certificate'});

    const thunk = updateCertificate(data);
    await thunk(dispatch, null, {client: axios, api: {certificateByIdUrl}});
    const [pending, fulfilled] = dispatch.mock.calls;
    const headers = axios.patch.mock.calls[0][2].headers;

    expect(headers['Content-Type']).toBe('multipart/form-data');
    expect(pending[0].type).toBe(updateCertificate.pending().type);
    expect(fulfilled[0].type).toBe(updateCertificate.fulfilled().type);
    expect(fulfilled[0].payload).toEqual({
      id: 1,
      name: 'Updated Certificate',
    });
  });

  test('update certificate fail', async () => {
    axios.patch.mockRejectedValue(new Error('This is error'));

    const thunk = updateCertificate(data);
    await thunk(dispatch, null, {client: axios, api: {certificateByIdUrl}});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(updateCertificate.pending().type);
    expect(rejected[0].type).toBe(updateCertificate.rejected().type);
    expect(rejected[0].payload).toBe('This is error');
  });
});

describe('delete certificate async actions', () => {
  test('delete certificate', async () => {
    const thunk = deleteCertificate();
    await thunk(dispatch, null, {client: axios, api: {certificateByIdUrl}});
    const [pending, fulfilled] = dispatch.mock.calls;

    expect(pending[0].type).toBe(deleteCertificate.pending().type);
    expect(fulfilled[0].type).toBe(deleteCertificate.fulfilled().type);
  });

  test('delete certificate fail', async () => {
    axios.delete.mockRejectedValue(new Error('This is error'));
    const thunk = deleteCertificate();
    await thunk(dispatch, null, {client: axios, api: {certificateByIdUrl}});
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(deleteCertificate.pending().type);
    expect(rejected[0].payload).toBe('This is error');
  });
});

describe('certificate reducer', () => {
  const initialState = createCertificateState(
    'received',
    '',
    certificateArray,
    currentPage
  );

  test('set page action', () => {
    const actual = certificatesReducer(initialState, setPage(3));
    const expected = createCertificateState('idle', '', certificateArray, {
      ...currentPage,
      number: 3,
    });

    expect(actual).toEqual(expected);
  });

  test('change page size action', () => {
    const actual = certificatesReducer(initialState, changePageSize(20));
    const expected = createCertificateState('idle', '', certificateArray, {
      ...currentPage,
      number: 0,
      size: 20,
    });

    expect(actual).toEqual(expected);
  });

  test('change status action', () => {
    const actual = certificatesReducer(initialState, changeStatus('created'));
    const expected = createCertificateState(
      'created',
      '',
      certificateArray,
      currentPage
    );

    expect(actual).toEqual(expected);
  });

  test('reset certificate action', () => {
    const actual = certificatesReducer(initialState, resetCertificates());
    const expected = createCertificateState('idle', '', [], {
      number: 0,
      size: 20,
      totalElements: 0,
      totalPages: 0,
    });

    expect(actual).toEqual(expected);
  });

  test('set page by url action', () => {
    const actual = certificatesReducer(
      initialState,
      setPageByUrl({number: 5, size: 50})
    );
    const expected = createCertificateState('idle', '', [], {
      ...currentPage,
      number: 4,
      size: 50,
    });

    expect(actual).toEqual(expected);
  });

  test('set page by url action with default page data', () => {
    const actual = certificatesReducer(initialState, setPageByUrl());

    const expected = createCertificateState('idle', '', [], {
      ...currentPage,
      number: 0,
      size: 20,
    });

    expect(actual).toEqual(expected);
  });
});

describe('certificate extra reducer load certificate action', () => {
  const response = {
    data: {
      _embedded: {
        certificateTagsDtoes: [
          {giftCertificateId: 9, name: 'Certificate9'},
          {giftCertificateId: 10, name: 'Certificate10'},
          {giftCertificateId: 11, name: 'Certificate11'},
        ],
      },
      page: {
        ...currentPage,
        number: 6,
      },
    },
  };

  test('load certificate pending action', () => {
    const initialState = createCertificateState(
      'rejected',
      'This is error',
      [],
      currentPage
    );

    const actual = certificatesReducer(
      initialState,
      loadCertificates.pending()
    );
    const expected = createCertificateState('loading', '', [], currentPage);

    expect(actual).toEqual(expected);
  });

  test('load certificate rejected action', () => {
    const initialState = createCertificateState(
      'loading',
      '',
      certificateArray,
      currentPage
    );

    const rejectedAction = {
      type: loadCertificates.rejected.type,
      payload: 'This is error',
    };

    const actual = certificatesReducer(initialState, rejectedAction);
    const expected = createCertificateState(
      'rejected',
      'This is error',
      [],
      currentPage
    );

    expect(actual).toEqual(expected);
  });

  test('load certificate rejected action canceled request', () => {
    const initialState = createCertificateState(
      'loading',
      '',
      certificateArray,
      currentPage
    );

    const rejectedAction = {
      type: loadCertificates.rejected.type,
      payload: 'canceled',
    };

    const actual = certificatesReducer(initialState, rejectedAction);
    const expected = createCertificateState('rejected', '', [], currentPage);

    expect(actual).toEqual(expected);
  });

  test('load certificate fulfilled action with infinite scroll', () => {
    const initialState = createCertificateState(
      'loading',
      '',
      certificateArray,
      currentPage
    );

    const actual = certificatesReducer(
      initialState,
      loadCertificates.fulfilled({response, infinite: true})
    );
    const expected = createCertificateState(
      'received',
      '',
      [...certificateArray, ...response.data._embedded.certificateTagsDtoes],
      {...currentPage, number: 6}
    );

    expect(actual).toEqual(expected);
  });

  test('load certificate fulfilled action', () => {
    const initialState = createCertificateState(
      'loading',
      '',
      certificateArray,
      currentPage
    );

    const actual = certificatesReducer(
      initialState,
      loadCertificates.fulfilled({response, infinite: false})
    );
    const expected = createCertificateState(
      'received',
      '',
      response.data._embedded.certificateTagsDtoes,
      {...currentPage, number: 6}
    );

    expect(actual).toEqual(expected);
  });
});

describe('certificate extra reducer other actions', () => {
  const initialState = createCertificateState('received', '', [], {
    number: 0,
    size: 20,
    totalPages: 0,
    totalElements: 0,
  });

  test('create certificate pending action', () => {
    const actual = certificatesReducer(
      initialState,
      createCertificate.pending()
    );
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('create certificate rejected action', () => {
    const rejectedAction = {
      type: createCertificate.rejected.type,
      payload: 'This is error',
    };

    const actual = certificatesReducer(initialState, rejectedAction);
    const expected = {
      ...initialState,
      status: 'rejected',
      error: 'This is error',
    };

    expect(actual).toEqual(expected);
  });

  test('create certificate fulfilled action', () => {
    const actual = certificatesReducer(
      initialState,
      createCertificate.fulfilled()
    );
    const expected = {...initialState, status: 'created'};

    expect(actual).toEqual(expected);
  });

  test('update certificate pending action', () => {
    const actual = certificatesReducer(
      initialState,
      updateCertificate.pending()
    );
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('update certificate rejected action', () => {
    const rejectedAction = {
      type: updateCertificate.rejected.type,
      payload: 'This is error',
    };

    const actual = certificatesReducer(initialState, rejectedAction);
    const expected = {
      ...initialState,
      status: 'rejected',
      error: 'This is error',
    };

    expect(actual).toEqual(expected);
  });

  test('update certificate fulfilled action', () => {
    const actual = certificatesReducer(
      initialState,
      updateCertificate.fulfilled()
    );
    const expected = {...initialState, status: 'updated'};

    expect(actual).toEqual(expected);
  });

  test('delete certificate pending action', () => {
    const actual = certificatesReducer(
      initialState,
      deleteCertificate.pending()
    );
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('delete certificate rejected action', () => {
    const rejectedAction = {
      type: deleteCertificate.rejected.type,
      payload: 'This is error',
    };

    const actual = certificatesReducer(initialState, rejectedAction);
    const expected = {
      ...initialState,
      status: 'rejected',
      error: 'This is error',
    };

    expect(actual).toEqual(expected);
  });

  test('delete certificate fulfilled action', () => {
    const actual = certificatesReducer(
      initialState,
      deleteCertificate.fulfilled()
    );
    const expected = {...initialState, status: 'deleted'};

    expect(actual).toEqual(expected);
  });
});
