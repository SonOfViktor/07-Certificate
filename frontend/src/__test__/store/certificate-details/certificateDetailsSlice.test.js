import axios from 'axios';
import {
  certificateDetailsReducer,
  clearDetails,
  loadCertificateById,
} from '../../../store/certificate-details/certificateDetailsSlice';

jest.mock('axios');

const initialState = {
  currentCertificate: null,
  status: 'idle',
  error: '',
};

const certificate = {
  id: 1,
  name: 'Certificate1',
};

describe('certificate details reducer', () => {
  test('clear state', () => {
    const currentState = {
      currentCertificate: certificate,
      status: 'received',
      error: '',
    };

    const actual = certificateDetailsReducer(currentState, clearDetails());

    expect(actual).toEqual(initialState);
  });
});

describe('load certificate async action', () => {
  const dispatch = jest.fn();
  const certificateByIdUrl = jest.fn().mockReturnValue('certificates/1');

  test('load certificate', async () => {
    axios.get.mockReturnValue(certificate);

    const thunk = loadCertificateById();
    await thunk(dispatch, null, {
      client: axios,
      api: {certificateByIdUrl: certificateByIdUrl},
    });
    const [pending, fulfilled] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadCertificateById.pending().type);
    expect(fulfilled[0].type).toBe(loadCertificateById.fulfilled().type);
    expect(fulfilled[0].payload).toEqual(certificate);
  });

  test('load certificate fail', async () => {
    axios.get.mockRejectedValue(new Error('This is error'));

    const thunk = loadCertificateById();
    await thunk(dispatch, null, {
      client: axios,
      api: {certificateByIdUrl: certificateByIdUrl},
    });
    const [pending, rejected] = dispatch.mock.calls;

    expect(pending[0].type).toBe(loadCertificateById.pending().type);
    expect(rejected[0].type).toBe(loadCertificateById.rejected().type);
    expect(rejected[0].error.message).toBe('This is error');
  });
});

describe('certificate details extra reducers', () => {
  test('pending load certificate details action', () => {
    const actual = certificateDetailsReducer(
      initialState,
      loadCertificateById.pending()
    );
    const expected = {...initialState, status: 'loading'};

    expect(actual).toEqual(expected);
  });

  test('fulfilled load certificate details action', () => {
    const actual = certificateDetailsReducer(
      initialState,
      loadCertificateById.fulfilled({data: certificate})
    );
    const expected = {
      currentCertificate: certificate,
      status: 'received',
      error: '',
    };

    expect(actual).toEqual(expected);
  });

  test('rejected load certificate details action', () => {
    const actual = certificateDetailsReducer(
      initialState,
      loadCertificateById.rejected(new Error('This is error'))
    );
    const expected = {
      currentCertificate: null,
      status: 'rejected',
      error: 'This is error',
    };

    expect(actual).toEqual(expected);
  });
});
