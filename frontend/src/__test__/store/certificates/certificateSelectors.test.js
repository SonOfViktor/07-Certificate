import {
  selectCertificatesInfo,
  selectCertificatesPage,
} from '../../../store/certificates/certificateSelectors';
import {certificateArray, createCertificateState, currentPage} from './testData';

describe('certificate selectors', () => {
  const state = {
    certificates: createCertificateState(
      'received',
      '',
      certificateArray,
      currentPage
    ),
  };

  test('select certificate info', () => {
    const actual = selectCertificatesInfo(state);
    const {page, ...expected} = state.certificates;

    expect(actual).toEqual(expected);
  });

  test('select certificate page', () => {
    const actual = selectCertificatesPage(state);

    expect(actual).toEqual(currentPage);
  });
});
