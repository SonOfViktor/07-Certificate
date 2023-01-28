import {selectCertificateDetails} from '../../../store/certificate-details/certificateDetailsSelectors';

describe('certificate details selector', () => {
  const state = {
    certificateDetails: {
      currentCertificate: {
        id: 1,
        name: 'Certificate1',
      },
      status: 'received',
      error: null,
    },
  };

  test('select certificate details', () => {
    expect(selectCertificateDetails(state)).toEqual(state.certificateDetails);
  });
});
