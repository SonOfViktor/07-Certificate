import {render, screen} from '@testing-library/react';
import SearchInput from '../../../components/header/SearchInput';

describe('search input component', () => {
  test('search input render', () => {
    render(<SearchInput />);

    expect(screen.getByPlaceholderText('Search by name')).toBeInTheDocument();
    expect(screen.getByTestId('SearchIcon')).toBeInTheDocument();
  });
});
