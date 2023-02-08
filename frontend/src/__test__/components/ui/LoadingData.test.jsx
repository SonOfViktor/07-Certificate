import {render, screen} from '@testing-library/react';
import LoadingData from '../../../components/ui/LoadingData';

describe('loading data component', () => {
  test('loading data when status is no loading', () => {
    const {container} = render(<LoadingData status="received" />);

    expect(container).toBeEmptyDOMElement();
  });

  test('loading data component with default status', () => {
    render(<LoadingData />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });

  test('loading data component when status loading', () => {
    render(<LoadingData status="loading" />);

    expect(screen.getByRole('progressbar')).toBeInTheDocument();
  });
});
