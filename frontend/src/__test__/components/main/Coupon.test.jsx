import {render, screen} from '@testing-library/react';
import * as routerHooks from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import {createCertificateList} from '../../helpers/certificateHelper';
import Coupon from '../../../components/main/Coupon';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('coupon component', () => {
  const certificate = createCertificateList(1)[0];

  const mockedUseNavigate = jest.spyOn(routerHooks, 'useNavigate');

  test('render coupon component', () => {
    render(<Coupon certificate={certificate} />);

    const image = screen.getByAltText('Certificate1');
    const title = screen.getByText('Certificate1');

    expect(image).toHaveAttribute('src', '/api/v1/certificates/1/image');
    expect(title).toBeInTheDocument();
  });

  test('click on image', () => {
    const navigator = jest.fn();
    mockedUseNavigate.mockReturnValue(navigator);

    render(<Coupon certificate={certificate} />);

    const image = screen.getByAltText('Certificate1');

    expect(navigator).not.toHaveBeenCalled();
    userEvent.click(image);
    expect(navigator).toHaveBeenCalledWith('details/1');
  });
});
