import {render, screen} from '@testing-library/react';
import {renderWithMui} from '../../../helpers/renderWithMui';
import userEvent from '@testing-library/user-event';
import * as reduxHooks from 'react-redux';
import CertificateTablePagination from '../../../../components/admin/certificate/CertificateTablePagination';

jest.mock('react-redux');

describe('certificate table pagination', () => {
  const tfoot = document.createElement('tfoot');
  const mockUseSelector = jest.spyOn(reduxHooks, 'useSelector');
  const certificatePage = {
    number: 5,
    size: 50,
    totalPages: 12,
  };

  beforeEach(() => {
    mockUseSelector.mockReturnValue(certificatePage);
  });

  test('certificate table pagination render', () => {
    render(<CertificateTablePagination />, {
      container: document.body.appendChild(tfoot),
    });

    const pageNumbers = screen.getAllByRole('listitem');
    const threeDots = screen.getAllByText('…');
    const currentSize = screen.getByText('50');

    expect(pageNumbers.length).toBe(11);
    expect(threeDots.length).toBe(2);
    expect(currentSize).toBeInTheDocument();
  });

  test('change size', () => {
    const changeSize = jest.fn();
    render(
      <CertificateTablePagination
        changePage={jest.fn}
        changeSize={changeSize}
      />, {
          container: document.body.appendChild(tfoot),
        }
    );

    expect(screen.getByText('50')).toBeInTheDocument();
    expect(screen.queryByText('20')).not.toBeInTheDocument();

    userEvent.click(screen.getByText('50'));

    const sizeOptions = screen.getAllByRole('option');
    userEvent.selectOptions(screen.getByRole('listbox'), sizeOptions[1]);

    expect(changeSize.mock.calls[0][0]).toBe(20);
  });

  test('active number page styles', () => {
    renderWithMui(<CertificateTablePagination />, {
      container: document.body.appendChild(tfoot),
    });

    const activeElement = screen.getByText('6');
    const element = screen.getByText('5');
    const activeElementStyles = getComputedStyle(activeElement);
    const elementStyles = getComputedStyle(element);

    expect(activeElementStyles.backgroundColor).toBe('rgb(64, 212, 126)');
    expect(elementStyles.backgroundColor).toBe('transparent');
  });

  test('change number page', () => {
    const changePage = jest.fn();

    render(<CertificateTablePagination changePage={changePage} />, {
      container: document.body.appendChild(tfoot),
    });

    const newPageNumber = screen.getByRole('button', {name: 'Go to page 12'});
    userEvent.click(newPageNumber);
    expect(changePage.mock.calls[0][0]).toBe(12);
  });

  test('change previous page', () => {
    const changePage = jest.fn();
    render(<CertificateTablePagination changePage={changePage} />, {
      container: document.body.appendChild(tfoot),
    });

    const previousPageNumber = screen.getByRole('button', {
      name: 'Go to previous page',
    });
    userEvent.click(previousPageNumber);
    expect(changePage.mock.calls[0][0]).toBe(5);
  });

  test('change next page', () => {
    const changePage = jest.fn();
    render(<CertificateTablePagination changePage={changePage} />, {
      container: document.body.appendChild(tfoot),
    });

    const nextPageNumber = screen.getByRole('button', {
      name: 'Go to next page',
    });
    userEvent.click(nextPageNumber);
    expect(changePage.mock.calls[0][0]).toBe(7);
  });
});
