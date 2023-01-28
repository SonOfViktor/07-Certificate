import {
  Checkbox,
  ListItem,
  ListItemIcon,
  ListItemText,
  MenuItem,
  Select,
} from '@mui/material';
import SortIcon from '@mui/icons-material/Sort';
import {useSelector} from 'react-redux';
import {useQueryParams} from '../hooks/useQueryParams';

import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import {selectSort} from '../../../store/filter/filterSelectors';

const Sorting = () => {
  const [changeQueryParams] = useQueryParams();
  const sorting = useSelector(selectSort);

  const handleFieldChange = e => {
    changeQueryParams('sort', `${e.target.value},${sorting.order}`);
  };

  const handleOrderChange = e => {
    changeQueryParams(
      'sort',
      `${sorting.field},${e.target.checked ? 'ASC' : 'DESC'}`
    );
  };

  return (
    <>
      <ListItem sx={{paddingBottom: 0}}>
        <ListItemIcon sx={{color: 'var(--primary)'}}>
          <SortIcon />
        </ListItemIcon>
        <ListItemText
          primary="Sorting"
          primaryTypographyProps={{
            fontSize: 16,
            fontWeight: 500,
          }}
        />
      </ListItem>
      <ListItem divider>
        <Select
          sx={{mr: 2}}
          fullWidth
          name="field"
          value={sorting.field}
          size="small"
          onChange={handleFieldChange}
          inputProps={{'aria-label': 'sort'}}>
          <MenuItem value="name">Name</MenuItem>
          <MenuItem value="price">Price</MenuItem>
          <MenuItem value="createDate">Create Date</MenuItem>
        </Select>
        <Checkbox
          name="order"
          icon={<KeyboardArrowDownIcon />}
          checkedIcon={<KeyboardArrowUpIcon />}
          sx={{color: 'var(--primary)'}}
          checked={sorting.order === 'ASC'}
          onChange={handleOrderChange}
        />
      </ListItem>
    </>
  );
};

export default Sorting;
