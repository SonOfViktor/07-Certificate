import {Stack, styled} from '@mui/material';
import {useEffect, useState} from 'react';
import {useLocation} from 'react-router-dom';
import CategorySelect from './CategorySelect';
import {useQueryParams} from './hooks/useQueryParams';
import SearchInput from './SearchInput';

const SearchForm = styled('div')({
  minWidth: '50%',
  flexShrink: '2',
  '@media screen and (max-width: 1150px)': {
    order: '3',
    width: '75%',
    marginRight: 'auto',
    marginLeft: 'auto',
  },
  '@media screen and (max-width: 600px)': {
    width: '95%',
  },
});

const Search = () => {
  const {pathname} = useLocation();
  const [changeQueryParams, initStateValue] = useQueryParams();
  const [search, setSearch] = useState(initStateValue('search', ''));

  useEffect(() => {
    return () => setSearch('');
  }, [pathname]);

  const handleSearch = e => {
    setSearch(e.target.value);
    changeQueryParams(e.target.name, e.target.value, 1000);
  };

  return (
    <SearchForm>
      <Stack direction="row">
        <SearchInput onChange={handleSearch} value={search} />
        <CategorySelect />
      </Stack>
    </SearchForm>
  );
};

export default Search;
