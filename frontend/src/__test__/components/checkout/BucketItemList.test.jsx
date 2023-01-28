import {render, screen} from '@testing-library/react';
import BucketItemList from '../../../components/checkout/BucketItemList';
import {createCertificateList} from '../../helpers/certificateHelper';

jest.mock('react-redux');

describe('bucket item list', () => {
  test('bucket item list render', () => {
    const certificates = createCertificateList(5);

    render(<BucketItemList certificates={certificates} />);

    expect(screen.getAllByText(/certificate/i).length).toBe(5);
  });
});
