import {render, screen} from '@testing-library/react';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as certificateActions from '../../../store/certificate-details/certificateDetailsSlice';
import {createCertificate} from '../../helpers/certificateHelper';
import Details from '../../../components/details/Details';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('certificate details', () => {
  const certificate = createCertificate(
    2,
    'Certificate',
    'some description',
    20,
    50,
    [{name: 'tag1'}, {name: 'tag2'}, {name: 'tag3'}]
  );
  certificate.category = 'Tech';

  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseParams = jest.spyOn(routerHooks, 'useParams');

  const dispatch = jest.fn();

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSelector.mockReturnValue({
      status: 'received',
      currentCertificate: certificate,
    });
    mockedUseParams.mockReturnValue({id: 2});
  });

  test('certificate details render', () => {
    render(<Details />);

    const image = screen.getByAltText('Certificate');
    const description = screen.getByText('some description');

    expect(image).toHaveAttribute('src', '/api/v1/certificates/2/image');
    expect(description).toBeInTheDocument();
  });

  test('load certificate when component mount', () => {
    const loadCertificateById = jest.spyOn(
      certificateActions,
      'loadCertificateById'
    );

    render(<Details />);

    expect(dispatch).toHaveBeenCalled();
    expect(loadCertificateById).toHaveBeenCalledWith(2);
  });

  test('clean details when component unmount', () => {
    const clearDetails = jest.spyOn(certificateActions, 'clearDetails');

    const {unmount} = render(<Details />);

    unmount();

    expect(dispatch).toHaveBeenCalled();
    expect(clearDetails).toHaveBeenCalled();
  });

  test('loading certificate details reject', () => {
    const navigator = jest.fn();
    mockedUseSelector.mockReturnValue({
      status: 'rejected',
    });
    const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');
    mockedUseNavigate.mockReturnValue(navigator);

    render(<Details />);

    expect(navigator).toHaveBeenCalledWith('/not-found');
  });
});
