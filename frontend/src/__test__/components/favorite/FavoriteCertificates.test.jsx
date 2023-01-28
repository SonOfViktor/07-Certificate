import {render, screen} from '@testing-library/react';
import {useSelector} from 'react-redux';
import FavoriteCertificates from '../../../components/favorite/FavoriteCertificates';
import {createCertificateList} from '../../helpers/certificateHelper';

jest.mock('react-redux', () => ({
  useSelector: jest.fn(),
  useDispatch: jest.fn(),
}));

describe('favorite certificates', () => {
  const certificates = createCertificateList(3);
  test('favorite certificates render', () => {
    useSelector.mockReturnValue(certificates);

    render(<FavoriteCertificates />);

    expect(screen.getAllByText(/certificate/i).length).toBe(3);
  });
});
