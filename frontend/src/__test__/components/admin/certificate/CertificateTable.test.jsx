import {screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import * as routerHooks from 'react-router-dom';
import * as certificateActions from '../../../../store/certificates/certificateSlice';
import {renderWithMui} from '../../../helpers/renderWithMui';
import CertificateTable from '../../../../components/admin/certificate/CertificateTable';
import {createCertificateList} from '../../../helpers/certificateHelper';

jest.mock('react-redux');
jest.mock('react-router-dom');

describe('coupon list component', () => {
  const mockedUseDispatch = jest.spyOn(reduxHooks, 'useDispatch');
  const mockedUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const mockedUseSearchParams = jest.spyOn(routerHooks, 'useSearchParams');
  const setPageAction = jest.spyOn(certificateActions, 'setPageByUrl');
  const dispatch = jest.fn();
  const setSearchParams = jest.fn();
  const certificateList = createCertificateList(3);

  const mockingUseSelector = status => {
    mockedUseSelector
      .mockReturnValueOnce({certificateList, status, error: ''})
      .mockReturnValueOnce(null)
      .mockReturnValue({number: 3, size: 50, totalPages: 8});
  };

  beforeEach(() => {
    mockedUseDispatch.mockReturnValue(dispatch);
    mockedUseSearchParams.mockReturnValue([
      {get: jest.fn().mockReturnValue(undefined)},
      setSearchParams,
    ]);
  });

  test('certificate table render', () => {
    mockingUseSelector('received');

    renderWithMui(<CertificateTable />);

    expect(screen.getByRole('table')).toBeInTheDocument();
    expect(setPageAction).toHaveBeenCalledWith({pageNumber: 1, size: 20});
  });

  test('certificate table side effects when status idle', () => {
    const loadCertificatesAction = jest.spyOn(
      certificateActions,
      'loadCertificates'
    );
    mockingUseSelector('idle');

    renderWithMui(<CertificateTable />);
    expect(loadCertificatesAction).toHaveBeenCalledWith({
      number: 0,
      size: 20,
      filter: null,
      infinite: false,
    });
  });

  test('certificate table when status updated', () => {
    const changeStatusAction = jest.spyOn(certificateActions, 'changeStatus');
    mockingUseSelector('updated');

    renderWithMui(<CertificateTable />);

    const successMessage = screen.getByText(
      'Certificate was updated successfully'
    );

    expect(changeStatusAction).toHaveBeenCalledWith('idle');
    expect(successMessage).toBeInTheDocument();
  });

  test('certificate table when status loading', () => {
    mockingUseSelector('loading');

    renderWithMui(<CertificateTable />);

    expect(screen.queryByRole('table')).toBeNull();
    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  test('change table page', () => {
    mockingUseSelector('received');

    renderWithMui(<CertificateTable />);

    const nextPageNumber = screen.getByRole('button', {
      name: 'Go to next page',
    });

    userEvent.click(nextPageNumber);

    expect(setSearchParams).toHaveBeenCalled();
  });

  test('change table size', () => {
    mockingUseSelector('received');

    renderWithMui(<CertificateTable />);

    const sizeSelector = screen.getByRole('button', {name: '50'});

    userEvent.click(sizeSelector);

    const sizeOptions = screen.getAllByRole('option');
    userEvent.selectOptions(screen.getByRole('listbox'), sizeOptions[1]);

    expect(setSearchParams).toHaveBeenCalled();
  });

  test('open delete modal window', () => {
    mockedUseSelector
      .mockReturnValueOnce({certificateList, status: 'received', error: ''})
      .mockReturnValueOnce(null)
      .mockReturnValueOnce({number: 3, size: 50, totalPages: 8})
      .mockReturnValueOnce({certificateList, status: 'received', error: ''})
      .mockReturnValueOnce(null)
      .mockReturnValue({number: 3, size: 50, totalPages: 8});

    renderWithMui(<CertificateTable />);

    const openDeleteModalWindowButtons = screen.getAllByTestId('DeleteIcon');

    expect(screen.queryByText('Delete Confirmation')).not.toBeInTheDocument();

    userEvent.click(openDeleteModalWindowButtons[1]);
    expect(screen.getByText('Delete Confirmation')).toBeInTheDocument();
  });

  test('open create modal window', () => {
    mockedUseSelector
      .mockReturnValueOnce({certificateList, status: 'received', error: ''})
      .mockReturnValueOnce(null)
      .mockReturnValueOnce({number: 3, size: 50, totalPages: 8})
      .mockReturnValueOnce({certificateList, status: 'received', error: ''})
      .mockReturnValueOnce(null)
      .mockReturnValueOnce({number: 3, size: 50, totalPages: 8})
      .mockReturnValue(['Sport', 'Food', 'Tech']);

    renderWithMui(<CertificateTable />);

    const openModalWindowButton = screen.getByTestId('NoteAddIcon');

    expect(screen.queryByText('Add New Certificate')).not.toBeInTheDocument();

    userEvent.click(openModalWindowButton);
    expect(screen.getByText('Add New Certificate')).toBeInTheDocument();
  });
});
